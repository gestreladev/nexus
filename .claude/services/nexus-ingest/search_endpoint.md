---
name: search_endpoint
description: GET /v1/search — semantic vector search over document chunks in nexus-ingest.
agent:
  role: fastapi-specialist
  tier: standard
  weight: soft
  triggers:
    - changing the search endpoint or its ranking
    - explaining the read path of vector search
metadata:
  type: reference
---

# Search Endpoint — `GET /v1/search`

The **read path** of Phase 9. Co-located with the embedders in nexus-ingest so
the query is embedded by the *same* model that embedded the documents (mixing
models breaks the distances).

## Contract
```
GET /v1/search?q=<text>&k=<1..50>
→ { "query": "...", "hits": [ { document_id, chunk_index, content, score }, ... ] }
```
- `score` = cosine similarity in `[-1, 1]` (higher = closer), computed as
  `1 - (embedding <=> $1)` in Postgres.
- `k` (default 5) caps the nearest-neighbor count.

## Flow
1. `embed_query(q)` — the **asymmetric** side (a query prompt / `input_type=query`),
   run in a worker thread so the model call never blocks the event loop.
2. `Database.search(vec, k)` → `ORDER BY embedding <=> $1 LIMIT k` over the HNSW
   index (see [pgvector.md](../../fundamentals/search/pgvector.md) · `[[pgvector]]`).
3. Map rows → `SearchHit` Pydantic models.

`db` and `embedder` are built once in the lifespan and read off `app.state`.

## In Nexus
The write path (`Ingestor`: chunk → embed → upsert) feeds this. A full-text
baseline + score fusion (hybrid) is the natural next step — see
[hybrid_search.md](../../fundamentals/search/hybrid_search.md) · `[[hybrid_search]]`.

## References
- [embedding_strategies.md](embedding_strategies.md) · `[[embedding_strategies]]`
- [nexus_ingest_orchestrator.md](nexus_ingest_orchestrator.md) · `[[nexus_ingest_orchestrator]]`
