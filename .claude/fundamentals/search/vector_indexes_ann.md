---
name: vector_indexes_ann
description: Why ANN indexes (HNSW, IVFFlat) make vector nearest-neighbor search sub-linear in pgvector.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - choosing or tuning a pgvector index (HNSW vs IVFFlat)
    - explaining why semantic search slows down at scale
metadata:
  type: reference
---

# Vector Indexes & Approximate Nearest Neighbor (ANN)

## The O(n) problem
Semantic search ranks rows by **distance** between a query vector and every stored
embedding (`ORDER BY embedding <=> $1 LIMIT k`). With no index, Postgres does a
sequential scan: it computes the distance for **all n** rows, then sorts. That is
**O(n) per query** (plus the sort). At a few thousand chunks it's fine; at millions
it collapses — every query touches the whole table.

## Why a B-tree can't help
Recall the index concept from Lesson 3 / [indexing.md](../databases/indexing.md) ·
`[[indexing]]`: a B-tree works because keys have a **total linear order**, so the
tree can binary-search and prune. A high-dimensional vector has **no single linear
order** — "nearness" is geometric (cosine/L2) across 1024 axes at once, and two
vectors can be close on some dimensions and far on others. There's no way to sort
them on a line such that neighbors-in-space are neighbors-in-order. So a B-tree
literally **cannot** index nearness. That's why pgvector ships a *new index species*
built for vector geometry rather than reusing B-tree.

## ANN: trade a little recall for sub-linear speed
**Exact** nearest neighbor over high-dim vectors is inherently expensive. **ANN
(Approximate Nearest Neighbor)** indexes give up the guarantee of returning the
*exact* top-k and instead return *almost always the right* top-k — in exchange for
**sub-linear** query time. The lever is **recall**: the fraction of true neighbors
actually returned. You tune recall up (slower, more accurate) or down (faster).
Both pgvector index types are ANN.

## HNSW vs IVFFlat

| | **HNSW** (2026 default) | **IVFFlat** |
|---|---|---|
| Structure | Navigable small-world **graph**, multi-layer | **Clusters/lists** (k-means centroids) |
| Query | Greedy graph walk toward the query | Probe nearest `lists`, scan their members |
| Training step | **None** — build incrementally | **Required** — needs data to fit centroids |
| Recall / latency | Excellent, robust | Good; sensitive to `lists`/`probes` |
| Build time | Slower | Faster |
| RAM | Higher (graph in memory) | Lighter |
| Tuning | `m`, `ef_construction`; query `ef_search` | `lists` (build); `probes` (query) |

**HNSW** navigates a graph where each node links to nearby neighbors plus a few
long-range "express" edges (the small-world property), so a walk reaches the target
region in roughly log steps. It needs **no training**, has strong recall/latency out
of the box, but costs more RAM and a slower build. **IVFFlat** partitions vectors
into `lists` clusters; a query only scans the few nearest clusters. It's lighter and
builds fast, but you must **train** it (centroids fit to existing data) and set
`lists`/`probes` well, and recall degrades if the data distribution shifts.

Rule of thumb in 2026: **default to HNSW** unless RAM or build time forces IVFFlat.

## pgvector operators (recap)
`vector(N)` column; distance operators: `<->` L2, `<#>` negative inner product,
`<=>` cosine. The index must be built for the operator class you query with.

```sql
CREATE INDEX ON document_chunks
  USING hnsw (embedding vector_cosine_ops);
```

## In Nexus
Phase 9 stores `document_chunks(embedding vector(1024))` and standardizes on **1024
dims** so local (mxbai) and Voyage embeddings are swappable without a re-embed
migration. The index is **HNSW with `vector_cosine_ops`**, matching the search query:

```sql
SELECT id, content
FROM document_chunks
ORDER BY embedding <=> $1   -- cosine distance, ANN-accelerated
LIMIT $2;
```

Upserts are idempotent, keyed on `(document_id, chunk_index)`. Picking HNSW means no
training step in the ingest path — a new chunk is just inserted and the graph
updates, which suits a streaming ingest pipeline. See
[pgvector.md](pgvector.md) · `[[pgvector]]` for column/operator mechanics and
[embeddings.md](embeddings.md) · `[[embeddings]]` for how the 1024-dim vectors are
produced.

## References
- Orchestrator: [search_orchestrator.md](search_orchestrator.md) · `[[search_orchestrator]]`
- Related leaves: [pgvector.md](pgvector.md) · `[[pgvector]]`,
  [embeddings.md](embeddings.md) · `[[embeddings]]`,
  [lexical_vs_semantic.md](lexical_vs_semantic.md) · `[[lexical_vs_semantic]]`,
  [hybrid_search.md](hybrid_search.md) · `[[hybrid_search]]`
- Index foundations: [indexing.md](../databases/indexing.md) · `[[indexing]]` ·
  [databases_orchestrator.md](../databases/databases_orchestrator.md) · `[[databases_orchestrator]]`
- Lesson log: [lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`
