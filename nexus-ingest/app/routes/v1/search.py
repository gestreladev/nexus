"""GET /v1/search — semantic search over document chunks (vector nearest-neighbor)."""

import asyncio

from fastapi import APIRouter, Query, Request
from pydantic import BaseModel

router = APIRouter()


class SearchHit(BaseModel):
    document_id: str
    chunk_index: int
    content: str
    score: float  # cosine similarity in [-1, 1]; higher = closer


class SearchResponse(BaseModel):
    query: str
    hits: list[SearchHit]


@router.get("/search", response_model=SearchResponse)
async def search(
    request: Request,
    q: str = Query(..., min_length=1, description="natural-language query"),
    k: int = Query(5, ge=1, le=50),
) -> SearchResponse:
    embedder = request.app.state.embedder
    db = request.app.state.db
    # embed_query (not embed_documents): the asymmetric retrieval side.
    query_vec = await asyncio.to_thread(embedder.embed_query, q)
    rows = await db.search(query_vec, k)
    hits = [
        SearchHit(
            document_id=str(r["document_id"]),
            chunk_index=r["chunk_index"],
            content=r["content"],
            score=float(r["score"]),
        )
        for r in rows
    ]
    return SearchResponse(query=q, hits=hits)
