---
name: hybrid_search
description: Fuse lexical and semantic retrieval (RRF) for recall and precision, with optional reranking.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - combining keyword and vector search into one result set
    - deciding how to rank or fuse multiple retrievers
metadata:
  type: reference
---

# Hybrid Search

Hybrid search runs **two retrievers over the same query** — a lexical one
(full-text / BM25, exact-token matching) and a semantic one (vector ANN over
embeddings) — and **fuses their results into a single ranking**. The point is
not redundancy; it is that the two retrievers fail differently:

| Retriever | Catches | Misses |
|-----------|---------|--------|
| Lexical   | exact tokens, IDs, rare terms, code symbols | paraphrase, synonyms, intent |
| Semantic  | meaning, paraphrase, cross-lingual intent   | exact rare tokens, out-of-vocab IDs |

Run either one alone and you lose on **both axes**: a query like
`error code E4011 timeout` needs the literal `E4011` (lexical) *and* the
"connection dropped" intent (semantic). Vector search may rank a chunk that
never contains `E4011`; lexical search may rank a chunk that says `E4011` but
is about a wholly different failure. Fusion keeps the documents that *either*
retriever is confident about, then lets agreement reinforce the top.

See [lexical_vs_semantic.md](lexical_vs_semantic.md) · `[[lexical_vs_semantic]]`
for the per-method tradeoffs, and [embeddings.md](embeddings.md) ·
`[[embeddings]]` for how the semantic vectors are produced.

## Why fuse ranks, not scores

Lexical scores (BM25, tf-idf) and vector distances (cosine `<=>` in pgvector)
live on **incompatible scales** — you cannot meaningfully add a BM25 score of
14.2 to a cosine distance of 0.18. Normalizing scores is brittle (distributions
shift per query). **Reciprocal Rank Fusion (RRF)** sidesteps this by using only
each document's *rank position* in each list, which is scale-free.

### Reciprocal Rank Fusion

For a document `d` appearing at rank `r` in each retriever's result list:

```
RRF(d) = Σ  1 / (k + rank_i(d))
        i
```

- `rank_i(d)` = 1-based position of `d` in retriever *i*'s list (top = 1).
- `k` = a smoothing constant, conventionally **60**. Larger `k` flattens the
  curve so deep ranks still contribute; smaller `k` sharply favors the top few.
- A document missing from a list contributes 0 for that retriever.

Sum across retrievers, sort descending by `RRF(d)`, take top-k. A document
ranked #1 by lexical and #3 by semantic with `k=60` scores
`1/61 + 1/63 ≈ 0.0323` — beating anything that only one retriever surfaced.
RRF is cheap, needs no training, and is robust precisely because it ignores the
raw scores.

## Reranking — optional final stage

Fusion gives a good candidate set; a **cross-encoder reranker** can reorder the
top ~50–100 for precision. Unlike the bi-encoders behind retrieval (query and
document embedded *separately*), a cross-encoder reads the query and candidate
*together* and scores relevance directly — far more accurate, far too slow to
run over the whole corpus. So the pipeline is:

```
query → [lexical topN ∥ semantic topN] → RRF fuse → rerank top100 → top-k
```

Reranking is strictly optional: ship fusion first, add a reranker only if
top-of-list precision is the bottleneck.

## In Nexus

**Phase 9** ships the **semantic half** plus a **full-text baseline** for
side-by-side comparison — deliberately *not* fused yet, so we can measure each
retriever in isolation before combining them. The semantic store is
`document_chunks(embedding vector(1024))`, standardized at **1024 dims** so the
local Matryoshka model (`mxbai-embed-large-v1`) and Voyage (`voyage-3-large`,
`output_dimension=1024`) are swappable without a re-embed migration. Retrieval
is `ORDER BY embedding <=> $1 LIMIT k` over an HNSW index
(`USING hnsw (embedding vector_cosine_ops)`); upserts are idempotent on
`(document_id, chunk_index)`.

**Fusion is the natural next step**: run the existing full-text baseline and the
vector query in parallel, then combine with RRF (`k=60`) — no new storage, just
a second query and a merge. A reranker would come after that, if needed. See
[pgvector.md](pgvector.md) · `[[pgvector]]` for the vector column/index details
and [vector_indexes_ann.md](vector_indexes_ann.md) · `[[vector_indexes_ann]]`
for the HNSW/IVFFlat ANN tradeoff that bounds semantic recall.

## References

- Search orchestrator: [search_orchestrator.md](search_orchestrator.md) · `[[search_orchestrator]]`
- [lexical_vs_semantic.md](lexical_vs_semantic.md) · `[[lexical_vs_semantic]]`
- [embeddings.md](embeddings.md) · `[[embeddings]]`
- [pgvector.md](pgvector.md) · `[[pgvector]]`
- [vector_indexes_ann.md](vector_indexes_ann.md) · `[[vector_indexes_ann]]`
- Lesson log: [lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`
- Databases: [databases_orchestrator.md](../databases/databases_orchestrator.md) · `[[databases_orchestrator]]` · [indexing.md](../databases/indexing.md) · `[[indexing]]`
