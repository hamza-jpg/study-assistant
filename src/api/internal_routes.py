"""Internal microservice API router for Spring Boot backend integration.

Exposes:
1. POST /internal/rag/stream - SSE token streaming with course isolation & conversation history
2. POST /internal/ingest - Background document ingestion into PgVector
3. POST /internal/flashcards/generate - Course-grounded flashcard generation
"""

from __future__ import annotations

import json
import logging
import os
from pathlib import Path
import re
import time
from typing import Any, Dict, Iterator, List, Optional
from fastapi import APIRouter, HTTPException, Query, Request
from fastapi.responses import StreamingResponse
from pydantic import BaseModel, Field

from src.ingestion.embedder import NvidiaEmbedder
from src.ingestion.loader import DocumentLoader
from src.ingestion.pipeline import IngestionPipeline
from src.ingestion.pgvector_indexer import PgVectorIndexer, DEFAULT_COURSE_ID
from src.retrieval.query_augmenter import QueryAugmenter
from src.retrieval.reranker import StudyReranker
from src.retrieval.retriever import StudyRetriever
from src.retrieval.tavily_search import TavilySearchClient
from src.generation.generator import StudyGenerator

logger = logging.getLogger(__name__)

internal_router = APIRouter(prefix="/internal", tags=["Internal RAG Microservice"])


# --- Pydantic Models for Internal Contracts ---

class InternalRAGStreamRequest(BaseModel):
    query: str = Field(..., description="Student's academic question")
    course_id: str = Field(DEFAULT_COURSE_ID, description="Target course UUID for multi-tenancy isolation")
    history: Optional[List[Dict[str, str]]] = Field(default=None, description="Previous conversation turns")
    top_k: int = Field(15, ge=1, le=50)
    top_n: int = Field(5, ge=1, le=20)
    augment: bool = Field(True)
    augment_mode: str = Field("expand")
    min_similarity: Optional[float] = Field(None)
    min_rerank_score: Optional[float] = Field(None)
    use_web: bool = Field(False)
    fallback_to_web: bool = Field(True)


class InternalIngestRequest(BaseModel):
    course_id: str = Field(..., description="Target course UUID")
    file_path: str = Field(..., description="Absolute or relative file path to ingest")
    document_id: Optional[str] = Field(None, description="Optional existing document UUID")
    job_id: Optional[str] = Field(None, description="Optional background job ID for tracing")


class InternalFlashcardsRequest(BaseModel):
    course_id: str = Field(..., description="Course UUID")
    topic: Optional[str] = Field(None, description="Topic to focus flashcards on")
    count: int = Field(4, ge=1, le=15, description="Number of flashcards to generate")


# --- Lazy Component Factory ---

def get_internal_rag_components(course_id: str = DEFAULT_COURSE_ID):
    """Initialize or reuse AI components configured for PgVector and multi-tenancy."""
    db_url = os.getenv("DATABASE_URL", "postgresql://postgres:postgrespassword@localhost:5432/study_assistant")
    indexer = PgVectorIndexer(db_url=db_url, default_course_id=course_id)

    # Initialize Embedder (fallback to offline mock embedder if keys not set)
    try:
        embedder = NvidiaEmbedder()
    except ValueError:
        from src.api.server import OfflineFallbackEmbedder
        embedder = OfflineFallbackEmbedder()

    # Ingestion Pipeline
    pipeline = IngestionPipeline(embedder=embedder, indexer=indexer)

    # Retriever with Query Augmentation
    try:
        augmenter = QueryAugmenter()
    except Exception:
        augmenter = None

    retriever = StudyRetriever(indexer=indexer, embedder=embedder, augmenter=augmenter)

    # Reranker
    try:
        reranker = StudyReranker(mode="llm_listwise")
    except Exception:
        from src.api.server import OfflineFallbackReranker
        reranker = OfflineFallbackReranker()

    # Generator
    try:
        generator = StudyGenerator()
    except Exception:
        from src.api.server import OfflineFallbackGenerator
        generator = OfflineFallbackGenerator()

    # Tavily Web Search
    tavily = TavilySearchClient()

    return {
        "indexer": indexer,
        "embedder": embedder,
        "pipeline": pipeline,
        "retriever": retriever,
        "reranker": reranker,
        "generator": generator,
        "tavily": tavily,
    }


# --- Internal Endpoints ---

@internal_router.post("/rag/stream")
def internal_rag_stream(req: InternalRAGStreamRequest):
    """Zero-buffering SSE token streaming endpoint consumed by Spring Boot BFF.

    Yields JSON-encoded SSE events:
    - {"type": "chunks", "chunks": [...]}
    - {"type": "token", "token": "..."}
    - {"type": "done", "sources": [...], "model": "..."}
    """
    comp = get_internal_rag_components(course_id=req.course_id)
    indexer = comp["indexer"]
    retriever = comp["retriever"]
    reranker = comp["reranker"]
    generator = comp["generator"]
    tavily = comp["tavily"]

    def event_generator() -> Iterator[str]:
        try:
            if not req.query or not req.query.strip():
                yield f"data: {json.dumps({'type': 'token', 'token': 'Please provide a valid question.'})}\n\n"
                yield f"data: {json.dumps({'type': 'done', 'sources': []})}\n\n"
                return

            total_chunks = indexer.count(course_id=req.course_id)

            # Handle empty course material
            if total_chunks == 0:
                if (req.use_web or req.fallback_to_web) and tavily and tavily.is_available:
                    web_chunks = tavily.search(query=req.query.strip(), max_results=req.top_n)
                    if web_chunks:
                        chunk_previews = [
                            {
                                "source": c.get("metadata", {}).get("source", "Web Source"),
                                "page": c.get("metadata", {}).get("page", 1),
                                "score": c.get("similarity_score", 0.7),
                                "preview": c.get("text", "")[:180] + "...",
                            }
                            for c in web_chunks
                        ]
                        yield f"data: {json.dumps({'type': 'chunks', 'chunks': chunk_previews})}\n\n"
                        context = retriever.format_context(web_chunks)
                        stream = generator.generate_stream(
                            query=req.query.strip(),
                            context=context,
                            history=req.history,
                        )
                        for token in stream:
                            yield f"data: {json.dumps({'type': 'token', 'token': token})}\n\n"
                        sources = [c.get("metadata", {}).get("source", "web") for c in web_chunks]
                        yield f"data: {json.dumps({'type': 'done', 'sources': sources})}\n\n"
                        return

                msg = "No course materials indexed for this course yet."
                yield f"data: {json.dumps({'type': 'token', 'token': msg})}\n\n"
                yield f"data: {json.dumps({'type': 'done', 'sources': []})}\n\n"
                return

            # 1. Retrieve candidates with course_id isolation
            candidates = retriever.retrieve(
                query=req.query.strip(),
                top_k=req.top_k,
                course_id=req.course_id,
                augment=req.augment,
                augment_mode=req.augment_mode,
            )

            # 2. Web search fallback if enabled
            if not candidates and req.fallback_to_web and tavily and tavily.is_available:
                candidates = tavily.search(query=req.query.strip(), max_results=req.top_n)

            # 3. Rerank
            if candidates:
                reranked = reranker.rerank(query=req.query.strip(), chunks=candidates, top_n=req.top_n)
            else:
                reranked = []

            # Stream candidate chunk previews
            chunk_previews = [
                {
                    "source": c.get("metadata", {}).get("source", "Course Material"),
                    "page": c.get("metadata", {}).get("page", 1),
                    "score": c.get("rerank_score", c.get("similarity_score", 0.0)),
                    "preview": c.get("text", "")[:180] + "...",
                }
                for c in reranked
            ]
            yield f"data: {json.dumps({'type': 'chunks', 'chunks': chunk_previews})}\n\n"

            # 4. Stream generation with conversational history
            context = retriever.format_context(reranked)
            stream = generator.generate_stream(
                query=req.query.strip(),
                context=context,
                history=req.history,
            )

            full_tokens: List[str] = []
            for token in stream:
                full_tokens.append(token)
                yield f"data: {json.dumps({'type': 'token', 'token': token})}\n\n"

            # Extract citations from generated text
            full_text = "".join(full_tokens)
            citations = re.findall(r"\[Source:\s*([^,\]]+),\s*Page:\s*(\d+)\]", full_text)
            unique_sources = sorted(list(set(f"{src} (p. {page})" for src, page in citations)))

            yield f"data: {json.dumps({'type': 'done', 'sources': unique_sources, 'model': getattr(generator, 'model', '')})}\n\n"

        except Exception as e:
            logger.exception("Error in internal RAG streaming")
            yield f"data: {json.dumps({'type': 'error', 'error': str(e)})}\n\n"

    return StreamingResponse(
        event_generator(),
        media_type="text/event-stream",
        headers={
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
            "X-Accel-Buffering": "no",
        },
    )


@internal_router.post("/ingest")
def internal_ingest(req: InternalIngestRequest) -> Dict[str, Any]:
    """Ingest a document into PgVector under a specific course_id."""
    target_path = Path(req.file_path).resolve()
    if not target_path.exists():
        raise HTTPException(status_code=404, detail=f"File not found: {req.file_path}")

    comp = get_internal_rag_components(course_id=req.course_id)
    pipeline = comp["pipeline"]
    indexer = comp["indexer"]

    # Delete existing chunks for this file under this course to avoid ghost chunks
    indexer.delete_by_path(file_path=str(target_path), course_id=req.course_id)

    # Ingest document
    raw_docs = pipeline.loader.load(target_path)
    chunks = pipeline.chunker.split_documents(raw_docs) if hasattr(pipeline.chunker, "split_documents") else pipeline.chunker(raw_docs)

    if not chunks:
        return {
            "success": True,
            "course_id": req.course_id,
            "chunks_created": 0,
            "total_course_chunks": indexer.count(course_id=req.course_id),
        }

    # Embed chunks in safe batches
    for i in range(0, len(chunks), pipeline.batch_size):
        batch = chunks[i : i + pipeline.batch_size]
        texts = [c.page_content for c in batch]
        embeddings = pipeline.embedder.embed_batch(texts, input_type="passage")
        for chunk, emb in zip(batch, embeddings):
            if not hasattr(chunk, "metadata") or not isinstance(chunk.metadata, dict):
                chunk.metadata = {}
            chunk.metadata["embedding"] = emb
            chunk.metadata["course_id"] = req.course_id
            chunk.metadata["file_path"] = str(target_path)
            chunk.metadata["source"] = target_path.name

        indexer.add_chunks(batch, course_id=req.course_id, document_id=req.document_id)

    total_chunks = indexer.count(course_id=req.course_id)
    return {
        "success": True,
        "course_id": req.course_id,
        "document_id": req.document_id,
        "job_id": req.job_id,
        "chunks_created": len(chunks),
        "total_course_chunks": total_chunks,
    }


@internal_router.post("/flashcards/generate")
def internal_generate_flashcards(req: InternalFlashcardsRequest) -> Dict[str, Any]:
    """Generate high-yield active-recall flashcards from course materials."""
    comp = get_internal_rag_components(course_id=req.course_id)
    indexer = comp["indexer"]
    retriever = comp["retriever"]
    generator = comp["generator"]

    if indexer.count(course_id=req.course_id) == 0:
        raise HTTPException(
            status_code=400,
            detail="No study materials found for this course. Ingest materials first.",
        )

    topic = req.topic or "core definitions, principles, and key equations"
    chunks = retriever.retrieve(query=topic, top_k=10, course_id=req.course_id)
    context = retriever.format_context(chunks)

    prompt = (
        f"You are an expert academic tutor. Based strictly on the provided context below, generate {req.count} "
        "high-yield active recall flashcards for exam preparation.\n\n"
        "Return ONLY a valid JSON array of objects with the following schema, and no other text:\n"
        '[\n  {\n    "question": "Clear concept question or definition prompt",\n'
        '    "answer": "Concise, accurate pedagogical explanation with key formulas if any",\n'
        '    "category": "Topic Name",\n'
        '    "difficulty": "Easy|Medium|Hard",\n'
        '    "source": "Filename and Page from context"\n  }\n]\n\n'
        f"Context:\n{context}\n\nFlashcards JSON:"
    )

    try:
        resp = generator.client.chat.completions.create(
            model=generator.model,
            messages=[
                {"role": "system", "content": "You are a precise academic study assistant that outputs strict JSON."},
                {"role": "user", "content": prompt},
            ],
            temperature=0.2,
        )
        content = resp.choices[0].message.content.strip()
        if content.startswith("```json"):
            content = content.replace("```json", "", 1).rsplit("```", 1)[0].strip()
        elif content.startswith("```"):
            content = content.replace("```", "", 1).rsplit("```", 1)[0].strip()

        cards = json.loads(content)
        return {"course_id": req.course_id, "flashcards": cards, "total": len(cards)}
    except Exception as e:
        logger.warning(f"LLM flashcard JSON generation failed: {e}. Using heuristic fallback.")
        cards = []
        for idx, chunk in enumerate(chunks[:req.count]):
            lines = [l.strip() for l in chunk.get("text", "").split("\n") if l.strip()]
            header = lines[0] if lines else f"Concept {idx + 1}"
            explanation = " ".join(lines[1:]) if len(lines) > 1 else header
            cards.append({
                "question": f"Explain: {header}",
                "answer": explanation[:300],
                "category": "Course Concept",
                "difficulty": "Medium",
                "source": chunk.get("metadata", {}).get("source", "Course Notes"),
            })
        return {"course_id": req.course_id, "flashcards": cards, "total": len(cards), "fallback": True}
