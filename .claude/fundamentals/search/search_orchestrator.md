---
name: search_orchestrator
description: Routing for search fundamentals — lexical vs semantic, embeddings, ANN, pgvector, hybrid.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - designing document search or retrieval
    - choosing an index/distance for vectors
    - grounding embeddings or hybrid-search concepts
metadata:
  type: orchestrator
---

# Search Fundamentals — Orchestrator

The durable, language-agnostic search knowledge taught in **Phase 9** (Search &
Vectors): how to retrieve documents by meaning, not just keywords. This is **the
knowledge itself** — distinct from `services/nexus-ingest/` (how Nexus implements
it). Model selection governed by
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Routing Table

| Topic | Scope | Document |
|---|---|---|
| Lexical vs semantic | Full-text (tsvector) vs vector search; when each wins | [lexical_vs_semantic.md](lexical_vs_semantic.md) · `[[lexical_vs_semantic]]` |
| Embeddings | text→vector; cosine; dimension contract; retrieval asymmetry | [embeddings.md](embeddings.md) · `[[embeddings]]` |
| Vector indexes (ANN) | Why O(n) fails; HNSW vs IVFFlat; approximate NN | [vector_indexes_ann.md](vector_indexes_ann.md) · `[[vector_indexes_ann]]` |
| pgvector | `vector(N)`, `<=>`, HNSW index, asyncpg round-trip | [pgvector.md](pgvector.md) · `[[pgvector]]` |
| Hybrid search | Fuse lexical + semantic (RRF); reranking | [hybrid_search.md](hybrid_search.md) · `[[hybrid_search]]` |

---

## How Nexus applies it
`nexus-ingest` chunks each document, embeds every chunk into a `vector(1024)`,
and stores it in `document_chunks` with an **HNSW cosine** index. Search embeds
the query (asymmetrically) and runs `ORDER BY embedding <=> $1`. A full-text
baseline rides alongside for comparison; fusing the two (hybrid) is the next step.

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`
- [databases_orchestrator.md](../databases/databases_orchestrator.md) · `[[databases_orchestrator]]`
