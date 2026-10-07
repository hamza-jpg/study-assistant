from __future__ import annotations

import json
import logging
import os
import uuid
from typing import Any, Dict, List, Optional, Union
from dotenv import load_dotenv
import psycopg
from pgvector.psycopg import register_vector

load_dotenv(".env.local")
load_dotenv()

logger = logging.getLogger(__name__)

DEFAULT_COURSE_ID = "00000000-0000-0000-0000-000000000001"
DEFAULT_DATABASE_URL = "postgresql://postgres:postgrespassword@localhost:5432/study_assistant"


class PgVectorIndexer:
    """Production-grade Vector Indexer powered by PostgreSQL and pgvector.

    Replaces ChromaDB to provide unified ACID persistence for:
    1. Multi-tenant document chunks partitioned by `course_id`.
    2. 2048-dimensional dense vector embeddings with HNSW indexing.
    3. Document metadata and page lineage.
    """

    def __init__(
        self,
        db_url: Optional[str] = None,
        default_course_id: str = DEFAULT_COURSE_ID,
    ):
        self.db_url = (
            db_url
            or os.getenv("DATABASE_URL")
            or DEFAULT_DATABASE_URL
        )
        self.default_course_id = default_course_id
        self._ensure_connection()

    def _get_connection(self) -> psycopg.Connection:
        """Create and return a configured psycopg connection with vector support."""
        conn = psycopg.connect(self.db_url)
        register_vector(conn)
        return conn

    def _ensure_connection(self) -> None:
        """Verify database connectivity on startup."""
        try:
            with self._get_connection() as conn:
                with conn.cursor() as cur:
                    cur.execute("SELECT 1")
        except Exception as e:
            logger.warning(f"Could not connect to PostgreSQL at {self.db_url}: {e}")

    def _extract_chunk_data(self, chunk: Any) -> Dict[str, Any]:
        """Extract standardized chunk fields from LangChain Document or dictionary."""
        # 1. Text extraction
        if hasattr(chunk, "page_content"):
            text = chunk.page_content
        elif isinstance(chunk, dict):
            text = chunk.get("text", chunk.get("page_content", ""))
        else:
            text = getattr(chunk, "text", "")

        # 2. Embedding extraction
        if hasattr(chunk, "embedding") and chunk.embedding is not None:
            embedding = chunk.embedding
        elif isinstance(chunk, dict) and "embedding" in chunk and chunk["embedding"] is not None:
            embedding = chunk["embedding"]
        elif hasattr(chunk, "metadata") and isinstance(chunk.metadata, dict) and "embedding" in chunk.metadata:
            embedding = chunk.metadata["embedding"]
        else:
            embedding = None

        # 3. ID extraction
        chunk_id = None
        if hasattr(chunk, "id") and chunk.id:
            chunk_id = str(chunk.id)
        elif hasattr(chunk, "chunk_id") and chunk.chunk_id:
            chunk_id = str(chunk.chunk_id)
        elif hasattr(chunk, "metadata") and isinstance(chunk.metadata, dict):
            chunk_id = str(chunk.metadata.get("id", chunk.metadata.get("chunk_id", ""))) or None
        elif isinstance(chunk, dict):
            chunk_id = str(chunk.get("id", chunk.get("chunk_id", ""))) or None

        # Ensure valid UUID
        try:
            if chunk_id:
                uuid.UUID(chunk_id)
            else:
                chunk_id = str(uuid.uuid4())
        except (ValueError, TypeError):
            # Deterministic UUID from non-uuid string (e.g. hash_0)
            chunk_id = str(uuid.uuid5(uuid.NAMESPACE_DNS, str(chunk_id)))

        # 4. Metadata extraction
        raw_meta: Dict[str, Any] = {}
        if hasattr(chunk, "metadata") and isinstance(chunk.metadata, dict):
            raw_meta = dict(chunk.metadata)
        elif isinstance(chunk, dict):
            if "metadata" in chunk and isinstance(chunk["metadata"], dict):
                raw_meta = dict(chunk["metadata"])
            else:
                reserved = {"text", "page_content", "embedding", "id", "chunk_id"}
                raw_meta = {k: v for k, v in chunk.items() if k not in reserved}

        raw_meta.pop("embedding", None)

        # Extract page number & chunk index
        page_number = int(raw_meta.get("page", raw_meta.get("page_number", 1)))
        chunk_index = int(raw_meta.get("chunk_index", 0))

        return {
            "id": chunk_id,
            "text": text,
            "embedding": embedding,
            "page_number": page_number,
            "chunk_index": chunk_index,
            "metadata": raw_meta,
        }

    def _ensure_document_record(
        self,
        conn: psycopg.Connection,
        course_id: str,
        file_path: str,
        file_name: str,
    ) -> str:
        """Find or create a document record in the documents table, returning its UUID."""
        with conn.cursor() as cur:
            cur.execute(
                """
                SELECT id FROM documents 
                WHERE course_id = %s::uuid AND file_path = %s
                LIMIT 1
                """,
                (course_id, file_path),
            )
            row = cur.fetchone()
            if row:
                return str(row[0])

            doc_id = str(uuid.uuid4())
            cur.execute(
                """
                INSERT INTO documents (id, course_id, file_name, file_path, status)
                VALUES (%s::uuid, %s::uuid, %s, %s, 'READY')
                """,
                (doc_id, course_id, file_name, file_path),
            )
            conn.commit()
            return doc_id

    def add_chunks(
        self,
        chunks: List[Any],
        course_id: Optional[str] = None,
        document_id: Optional[str] = None,
    ) -> int:
        """Upsert document chunks into the PostgreSQL document_chunks table."""
        if not chunks:
            return 0

        target_course = course_id or self.default_course_id

        with self._get_connection() as conn:
            # Resolve or create document_id if not given
            first_meta = chunks[0].metadata if hasattr(chunks[0], "metadata") else (chunks[0] if isinstance(chunks[0], dict) else {})
            file_path = str(first_meta.get("file_path", first_meta.get("source", "unknown_source")))
            file_name = str(first_meta.get("source", os.path.basename(file_path)))

            actual_doc_id = document_id or self._ensure_document_record(
                conn=conn,
                course_id=target_course,
                file_path=file_path,
                file_name=file_name,
            )

            with conn.cursor() as cur:
                insert_query = """
                INSERT INTO document_chunks (
                    id, document_id, course_id, chunk_index, page_number, chunk_text, embedding, metadata
                ) VALUES (
                    %s::uuid, %s::uuid, %s::uuid, %s, %s, %s, %s, %s
                )
                ON CONFLICT (id) DO UPDATE SET
                    chunk_text = EXCLUDED.chunk_text,
                    embedding = EXCLUDED.embedding,
                    metadata = EXCLUDED.metadata,
                    page_number = EXCLUDED.page_number;
                """
                for chunk in chunks:
                    data = self._extract_chunk_data(chunk)
                    cur.execute(
                        insert_query,
                        (
                            data["id"],
                            actual_doc_id,
                            target_course,
                            data["chunk_index"],
                            data["page_number"],
                            data["text"],
                            data["embedding"],
                            json.dumps(data["metadata"]),
                        ),
                    )

                # Update document chunk count
                cur.execute(
                    """
                    UPDATE documents 
                    SET chunk_count = (SELECT count(*) FROM document_chunks WHERE document_id = %s::uuid),
                        updated_at = CURRENT_TIMESTAMP
                    WHERE id = %s::uuid
                    """,
                    (actual_doc_id, actual_doc_id),
                )
                conn.commit()

        return len(chunks)

    def query(
        self,
        query_embedding: List[float],
        top_k: int = 5,
        course_id: Optional[str] = None,
        where: Optional[Dict[str, Any]] = None,
    ) -> List[Dict[str, Any]]:
        """Retrieve nearest candidate chunks using pgvector cosine distance (<=>)."""
        target_course = course_id or self.default_course_id

        # Query uses cosine distance operator <=> in pgvector
        # Cosine distance = 1 - cosine_similarity (0.0 means identical, 1.0 means orthogonal)
        sql = """
        SELECT 
            id,
            chunk_text,
            metadata,
            page_number,
            (embedding <=> %s::vector) AS distance
        FROM document_chunks
        WHERE (%s::uuid IS NULL OR course_id = %s::uuid)
        """
        params: List[Any] = [query_embedding, target_course, target_course]

        # Additional metadata filters if provided
        if where:
            if "source" in where:
                sql += " AND metadata->>'source' = %s"
                params.append(where["source"])
            if "file_path" in where:
                sql += " AND metadata->>'file_path' = %s"
                params.append(where["file_path"])

        sql += """
        ORDER BY embedding <=> %s::vector
        LIMIT %s;
        """
        params.extend([query_embedding, top_k])

        formatted_results: List[Dict[str, Any]] = []
        with self._get_connection() as conn:
            with conn.cursor() as cur:
                cur.execute(sql, params)
                rows = cur.fetchall()
                for row in rows:
                    chunk_id, text, metadata_val, page_number, distance = row
                    meta = metadata_val if isinstance(metadata_val, dict) else (json.loads(metadata_val) if metadata_val else {})
                    meta["page"] = page_number

                    formatted_results.append({
                        "id": str(chunk_id),
                        "text": text,
                        "metadata": meta,
                        "distance": float(distance) if distance is not None else 0.0,
                    })

        return formatted_results

    def count(self, course_id: Optional[str] = None) -> int:
        """Return the total number of document chunks."""
        sql = "SELECT count(*) FROM document_chunks"
        params: List[Any] = []
        if course_id:
            sql += " WHERE course_id = %s::uuid"
            params.append(course_id)

        with self._get_connection() as conn:
            with conn.cursor() as cur:
                cur.execute(sql, params)
                return cur.fetchone()[0]

    def delete_by_path(self, file_path: str, course_id: Optional[str] = None) -> None:
        """Delete chunks originating from a specific file path."""
        target_course = course_id or self.default_course_id
        with self._get_connection() as conn:
            with conn.cursor() as cur:
                cur.execute(
                    """
                    DELETE FROM document_chunks
                    WHERE course_id = %s::uuid 
                      AND (metadata->>'file_path' = %s OR metadata->>'source' = %s)
                    """,
                    (target_course, str(file_path), str(file_path)),
                )
                conn.commit()

    def reset(self, course_id: Optional[str] = None) -> None:
        """Clear chunks for a specific course or entire database."""
        with self._get_connection() as conn:
            with conn.cursor() as cur:
                if course_id:
                    cur.execute("DELETE FROM document_chunks WHERE course_id = %s::uuid", (course_id,))
                else:
                    cur.execute("TRUNCATE TABLE document_chunks CASCADE")
                conn.commit()
