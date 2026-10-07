from unittest.mock import MagicMock, patch
from fastapi.testclient import TestClient
import pytest

from src.api.server import app

client = TestClient(app)


def test_internal_ingest_sample():
    """Test /internal/ingest with a real sample file."""
    payload = {
        "course_id": "00000000-0000-0000-0000-000000000001",
        "file_path": "sample_materials/astronomy_kepler.txt",
        "job_id": "job-test-123",
    }
    response = client.post("/internal/ingest", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["success"] is True
    assert data["chunks_created"] > 0
    assert data["course_id"] == "00000000-0000-0000-0000-000000000001"


def test_internal_rag_stream():
    """Test /internal/rag/stream SSE response."""
    payload = {
        "course_id": "00000000-0000-0000-0000-000000000001",
        "query": "What is Kepler's first law?",
        "history": [{"role": "user", "content": "Hello"}, {"role": "assistant", "content": "Hi there!"}],
        "top_k": 5,
        "top_n": 2,
    }
    response = client.post("/internal/rag/stream", json=payload)
    assert response.status_code == 200
    assert "text/event-stream" in response.headers["content-type"]
    content = response.text
    assert "data: " in content
    assert '"type": "token"' in content or '"type": "chunks"' in content


def test_internal_flashcards_generate():
    """Test /internal/flashcards/generate endpoint."""
    payload = {
        "course_id": "00000000-0000-0000-0000-000000000001",
        "topic": "planetary orbits",
        "count": 2,
    }
    response = client.post("/internal/flashcards/generate", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert "flashcards" in data
    assert len(data["flashcards"]) > 0
    assert data["course_id"] == "00000000-0000-0000-0000-000000000001"
