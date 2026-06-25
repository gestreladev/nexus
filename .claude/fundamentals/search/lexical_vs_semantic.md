---
name: lexical_vs_semantic
description: Lexical (full-text) vs semantic (vector) search, when each wins, and why production runs both.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - choosing between full-text and vector search for a query type
    - explaining why a keyword query missed a paraphrase (or vice versa)
    - designing Nexus chunk retrieval over code + prose
metadata:
  type: reference
---

# Lexical vs Semantic Search

Two retrieval paradigms answer "which documents match this query?" in
fundamentally different ways. They fail on opposite inputs, which is exactly
why production systems run both and fuse the results.

## Lexical (full-text) search

Matches **lexemes** — normalized word tokens. Postgres models the document as a
`tsvector` (lexemes + positions) and the query as a `tsquery`, then tests
containment with `@@`:

```sql
SELECT id FROM document_chunks
WHERE to_tsvector('english', body) @@ plainto_tsquery('english', 'kafka broker');
-- GIN index makes the @@ containment test sub-linear:
CREATE INDEX ON document_chunks USING gin (to_tsvector('english', body));
```

Properties: exact token matching, stemming/stop-words, `ts_rank` scoring. A
**GIN** inverted index maps each lexeme to its postings list. It is *exact* —
if the token is present, it is found; if absent, it is not. No notion of
meaning. `kafka:29092` and "message broker port" are unrelated strings to it.

## Semantic (vector) search

Encodes text into a dense **embedding** so that *meaning*, not surface form,
drives matching. Query and documents become vectors; nearest neighbours by
cosine/inner-product distance are the results. See [embeddings.md](embeddings.md) · `[[embeddings]]`.

```sql
SELECT id FROM document_chunks ORDER BY embedding <=> $1 LIMIT 8;  -- <=> = cosine
```

pgvector stores `vector(N)` and indexes with **HNSW** (the 2026 default —
strong recall/latency, no training step) or IVFFlat (clustering, needs a
training step). Both are **ANN** (approximate NN): trade a little recall for
sub-linear speed. Details in [vector_indexes_ann.md](vector_indexes_ann.md) · `[[vector_indexes_ann]]`
and [pgvector.md](pgvector.md) · `[[pgvector]]`.

Semantic search tolerates paraphrase, synonyms, and typos — but it can *blur*
exact identifiers, since an embedding compresses surface tokens into meaning.

## When each wins

| Query shape | Lexical | Semantic |
|---|---|---|
| Exact token / identifier (`kafka:29092`) | ✅ exact hit | ⚠️ may blur |
| Error string / stack trace fragment | ✅ | ⚠️ |
| Rare/OOV term, code symbol | ✅ | ⚠️ |
| Paraphrase / intent ("how to run locally") | ❌ misses | ✅ |
| Synonyms ("spin up" ≈ "start") | ❌ | ✅ |
| Typos / morphology | ⚠️ stemming only | ✅ |
| Multilingual / conceptual match | ❌ | ✅ |

Heuristic: **lexical for the literal, semantic for the latent.** If the user
likely typed the exact characters that appear in the answer, lexical wins. If
they described what they want in their own words, semantic wins.

## Complementary, not competing

Neither is strictly better. Lexical guarantees exact-token recall but is blind
to meaning; semantic captures meaning but can miss a precise identifier sitting
in plain text. Production retrieval runs both and merges scores (e.g. RRF) — see
[hybrid_search.md](hybrid_search.md) · `[[hybrid_search]]`. The GIN/inverted-index
intuition connects to general DB indexing: [indexing.md](../databases/indexing.md) · `[[indexing]]`.

## In Nexus

Nexus indexes both code and prose chunks, so both failure modes are live:

- **Code tokens favour lexical.** A query for `kafka:29092` (a literal broker
  address from docker config) must hit the chunk that contains that exact
  string. An embedding would happily return "messaging configuration" chunks
  while missing the line with the port. Messaging context:
  [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]`.
- **Intent favours semantic.** "how to run locally" shares almost no lexemes
  with the chunk `docker compose up`, yet that *is* the answer. Lexical returns
  nothing; semantic ranks it first.

Phase 9 schema: `document_chunks(embedding vector(1024))`. We standardize on
**1024** dims so a local Matryoshka model (mxbai-embed-large-v1) and Voyage
(`voyage-3-large`, `output_dimension=1024`) are swappable without a re-embed
migration. Upserts are idempotent, keyed on `(document_id, chunk_index)`; search
is `ORDER BY embedding <=> $1 LIMIT k`. The lexical side adds a `tsvector` GIN
index over the same chunks, and hybrid fusion combines them.

## References

- Orchestrator: [search_orchestrator.md](search_orchestrator.md) · `[[search_orchestrator]]`
- Related leaves: [embeddings.md](embeddings.md) · `[[embeddings]]`,
  [vector_indexes_ann.md](vector_indexes_ann.md) · `[[vector_indexes_ann]]`,
  [pgvector.md](pgvector.md) · `[[pgvector]]`,
  [hybrid_search.md](hybrid_search.md) · `[[hybrid_search]]`
- DB foundation: [databases_orchestrator.md](../databases/databases_orchestrator.md) · `[[databases_orchestrator]]`,
  [indexing.md](../databases/indexing.md) · `[[indexing]]`
- Lesson log: [lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`
