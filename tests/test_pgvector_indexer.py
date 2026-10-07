import pytest
from langchain_core.documents import Document
from src.ingestion.pgvector_indexer import PgVectorIndexer, DEFAULT_COURSE_ID


@pytest.fixture
def mock_embedding_2048():
    vec = [0.0] * 2048
    vec[0] = 1.0
    return vec


@pytest.fixture
def mock_chunks(mock_embedding_2048):
    other_vec = [0.0] * 2048
    other_vec[1] = 1.0
    return [
        Document(
            page_content="Calculus derivatives and integral calculation methods.",
            metadata={
                "source": "math_test.txt",
                "file_path": "math_test.txt",
                "embedding": mock_embedding_2048,
                "id": "doc_math",
                "page": 1,
            },
        ),
        Document(
            page_content="Physics dynamics and Newton's laws of motion.",
            metadata={
                "source": "physics_test.txt",
                "file_path": "physics_test.txt",
                "embedding": other_vec,
                "id": "doc_phys",
                "page": 2,
            },
        ),
    ]


@pytest.fixture
def pg_indexer():
    indexer = PgVectorIndexer()
    indexer.reset()
    yield indexer
    indexer.reset()


def test_pgvector_indexer_add_and_query(pg_indexer, mock_chunks, mock_embedding_2048):
    """Test adding chunks to pgvector and cosine distance querying."""
    added = pg_indexer.add_chunks(mock_chunks)
    assert added == 2
    assert pg_indexer.count() == 2

    # Query matching calculus chunk
    results = pg_indexer.query(query_embedding=mock_embedding_2048, top_k=1)
    assert len(results) == 1
    assert "Calculus" in results[0]["text"]
    assert results[0]["metadata"]["source"] == "math_test.txt"
    assert results[0]["distance"] < 0.01  # Exact match cosine distance ~ 0


def test_pgvector_indexer_filtering(pg_indexer, mock_chunks, mock_embedding_2048):
    """Test course_id and source metadata filtering."""
    pg_indexer.add_chunks(mock_chunks)

    # Filter by source
    results = pg_indexer.query(
        query_embedding=mock_embedding_2048,
        top_k=2,
        where={"source": "physics_test.txt"},
    )
    assert len(results) == 1
    assert "Physics" in results[0]["text"]


def test_pgvector_indexer_delete_by_path(pg_indexer, mock_chunks):
    """Test deleting chunks by file path."""
    pg_indexer.add_chunks(mock_chunks)
    assert pg_indexer.count() == 2

    pg_indexer.delete_by_path("math_test.txt")
    assert pg_indexer.count() == 1

    remaining = pg_indexer.query(query_embedding=[0.0] * 2048, top_k=5)
    assert len(remaining) == 1
    assert "Physics" in remaining[0]["text"]
