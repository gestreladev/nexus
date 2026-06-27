---
name: embeddings
description: Text→fixed-length vectors where nearby = similar meaning; the practical contract behind semantic search.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - choosing or swapping an embedding model for semantic search
    - debugging poor recall, dimension mismatches, or re-embed migrations
metadata:
  type: reference
---
# Embeddings

An **embedding** maps text to a fixed-length vector of floats. The defining property: **similar meaning ⇒ nearby vectors**, where "near" is measured by **cosine similarity** (angle between vectors). "king" and "monarch" land close; "king" and "carburetor" land far.

## The locality intuition

An embedding is **locality-preserving**: small changes in meaning ⇒ small changes in the vector. This is the exact *opposite* of a cryptographic hash, which is built for the **avalanche effect** — flip one input bit and ~half the output bits flip, deliberately destroying any neighborhood structure. A hash answers "are these byte-identical?"; an embedding answers "do these *mean* the same thing?". Search needs the latter.

## Five facts that drive a build

| # | Fact | Why it matters |
|---|------|----------------|
| 1 | **Same model for index + query.** | Two models produce incomparable coordinate spaces. Indexing with model A and querying with model B yields garbage neighbors even if dims match. |
| 2 | **Dimension is fixed per model** — it's a **schema contract**. | `vector(1024)` is baked into the column. Switching to a model with a different *native* dim = re-embed every row (a migration), not a config flip. |
| 3 | **Normalize ⇒ cosine = dot product.** | Unit-length vectors (‖v‖=1) make cosine similarity equal the inner product. Lets you use the cheaper operator and reason about distance consistently. |
| 4 | **Retrieval is asymmetric.** | A document and a search query are different *kinds* of text. Good models embed them with different prompts (`input_type`/query-prompt) so a short question lands near the long passage that answers it. |
| 5 | **Matryoshka truncation hits a target dim.** | Matryoshka-trained models pack the most information into the leading coordinates, so you can *truncate* a 1024-native vector to e.g. 512 or 256 with graceful quality loss — no separate model. |

### Normalization, concretely
cosine(a, b) = (a·b) / (‖a‖‖b‖). If both are unit vectors the denominator is 1, so **cosine = a·b**. Normalize once at write time and your distance math simplifies everywhere downstream.

### Asymmetry, concretely
```python
# Voyage: documents and queries get different prompts
client.embed(passages, model="voyage-3-large", input_type="document",
             output_dimension=1024)
client.embed([question], model="voyage-3-large", input_type="query",
             output_dimension=1024)
```

### Matryoshka, concretely
```python
from sentence_transformers import SentenceTransformer
m = SentenceTransformer("mixedbread-ai/mxbai-embed-large-v1")  # 1024 native
m.encode(texts, normalize_embeddings=True, truncate_dim=512)   # shorter vectors
```
For reference: `all-MiniLM-L6-v2` = 384 dim (no truncation needed/available).

## In Nexus

Phase 9 standardizes on **1024-dim** embeddings. This is a deliberate contract: the local model (**mxbai-embed-large-v1**, 1024 native) and the hosted model (**voyage-3-large** with `output_dimension=1024`) both produce 1024-dim vectors, so they are **swappable without a re-embed migration**. Schema and pipeline:

```sql
-- one column, one fixed dimension = the contract
document_chunks(embedding vector(1024))
```

- **Idempotent upsert** keyed on `(document_id, chunk_index)` — re-running ingest replaces rather than duplicates.
- **Search** is a single ordered scan against the ANN index:
  ```sql
  ORDER BY embedding <=> $1 LIMIT k   -- <=> is pgvector cosine distance
  ```
- Because everything is normalized to unit length, cosine ordering is stable across both models.

> Note: keeping the *same* native dimension across candidate models is what makes the swap cheap. A model whose native dim ≠ 1024 (and that isn't Matryoshka-truncatable to 1024) would force a full re-embed.

## Common failure modes

- **Mismatched models** → silently bad recall (fact 1). The vectors round-trip fine; the *meaning* doesn't.
- **Dim drift** → insert errors or, worse, a forced migration (fact 2).
- **Forgot normalization** → cosine vs dot-product confusion; rankings look "almost right but off."
- **Same prompt for doc and query** → weaker matches on short queries (fact 4).

## References

- Up: [search_orchestrator.md](search_orchestrator.md) · `[[search_orchestrator]]`
- How vectors get indexed for sub-linear search: `[[vector_indexes_ann]]`
- Postgres operators, ops classes, asyncpg round-trip: `[[pgvector]]`
- Where embeddings sit vs keyword matching: `[[lexical_vs_semantic]]` · combining both: `[[hybrid_search]]`
- Build log: [lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`
- DB fundamentals: [indexing.md](../databases/indexing.md) · `[[indexing]]` · [databases_orchestrator.md](../databases/databases_orchestrator.md) · `[[databases_orchestrator]]`
