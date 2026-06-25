---
name: lesson_10_search
description: Lesson 10 log — search & vectors; pgvector, embeddings, Strategy pattern.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 10 covered
    - recapping embeddings/pgvector/Strategy before a session
metadata:
  type: reference
---

# Lesson 10 — Search & Vectors (Phase 9)

| Field | Value |
|---|---|
| Phase | 9 — Search & Vectors |
| Roadmap topics | search-engines, elasticsearch, vectors, embeddings |
| Deliverable | Semantic search: chunk → embed → store (pgvector) → query |
| Milestone | `v0.9.0` |
| Status | ✅ mastery pass — semantic search proven live with the local model |

## Concepts taught
- **Lexical vs semantic** — full-text (tsvector, exact lexemes) vs vector (meaning);
  complementary, not rival; hybrid (fuse scores) is production's answer.
- **Embeddings** — text→vector where similar meaning = nearby (cosine); locality-
  preserving (inverse of a crypto hash). Same model for index+query; fixed dim is
  a schema contract; retrieval **asymmetry** (document vs query embedding).
- **ANN indexes** — naive NN is O(n)/query; HNSW (graph, 2026 default) / IVFFlat
  trade a little recall for sub-linear speed. A B-tree can't order high-dim nearness.
- **pgvector** — `vector(1024)`, `<=>` cosine, HNSW `vector_cosine_ops`; asyncpg
  `register_vector` round-trips vectors.
- **Strategy pattern (Rule 7)** — embedding + chunking are swappable strategies.

## Exercises (recap, from session start)
| Q | Topic | Verdict |
|---|---|---|
| R1 | Kafka dual-listener mechanism (advertised address must be reachable) | 🔁 re-taught (pt-BR) |
| R2a | secrets out of the committed file (each env injects its own) | ✅ |
| R2b | `${VAR:?}` = required, fail-fast vs empty substitution | ✅ taught |
| R3 | at-least-once → embedding write must be **idempotent** (upsert) | 🔁 taught → build req |
| E1 | semantic worse at exact terms; keep lexical | ✅ |
| E2 | the missing primitive is the **index** (ANN), not a foreign key | 🔁 corrected |

## Built — Stage 1 (verified)
- `nexus-api` Flyway **V5** (documents.content) + **V6** (`document_chunks`,
  `vector(1024)`, HNSW cosine index, `UNIQUE(document_id, chunk_index)`). Applied,
  schema confirmed.
- `nexus-api` now **stores + returns** document `content` (was silently dropped).
- **Flyway fat-jar fix**: `mergeServiceFiles()` — shadow-merge had clobbered
  Flyway's `ServiceLoader` plugin file (3 of 30 entries), dropping the SQL
  resolver so migrations never resolved. Service file 3→30; V5/V6 apply.
- `nexus-ingest` **Strategy layer**: `EmbeddingStrategy` (embed_documents +
  embed_query) with Local (mxbai 1024) / Voyage (output_dimension=1024) / Fake
  adapters + factory; `ChunkingStrategy` + `CharacterChunker`; `db.py`
  (asyncpg + pgvector, idempotent upsert, cosine `search`). mypy strict + ruff
  clean; 12 unit tests pass.

## Built — Stage 2 (verified)
- `Ingestor` (fetch → chunk → embed → idempotent upsert) drives the consumer;
  `GET /v1/search` (embed_query → cosine NN). `db`/`embedder` built in lifespan.
- Integration test (Fake + live Postgres): pipeline, **idempotency** (re-process →
  same count), search ranking. 13 tests green; mypy strict + ruff clean.
- **Live proof (local mxbai model, 9.6 GB image, 2 GB RAM):** posted 3 topic docs;
  queries with *different words* ranked the right topic first every time —
  "run on my computer"→docker (0.628), "keeps logins safe"→bcrypt/JWT (0.693),
  "read performance"→redis (0.547). Pure semantic match.

## Gaps / next
- Full-text baseline + hybrid (RRF) fusion — the natural Phase 9 follow-up.
- Embedding provider runs `local`; flip `NEXUS_EMBEDDING_PROVIDER=voyage` (+ key)
  to swap with no re-embed (the Strategy's payoff).

## References
- [lesson_09_containers.md](lesson_09_containers.md) · `[[lesson_09_containers]]`
- [search_orchestrator.md](../../fundamentals/search/search_orchestrator.md) · `[[search_orchestrator]]`
- [phase_09.md](../../project/phases/phase_09.md) · `[[phase_09]]`
