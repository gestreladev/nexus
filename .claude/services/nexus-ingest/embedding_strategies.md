---
name: embedding_strategies
description: Strategy pattern for swappable embedders (local/voyage/fake) standardized on dim 1024.
agent:
  role: fastapi-specialist
  tier: standard
  weight: medium
  triggers:
    - choosing or swapping an embedding backend in nexus-ingest
    - implementing embed_documents vs embed_query retrieval asymmetry
    - wiring a new EmbeddingStrategy adapter or the build_embedder factory
metadata:
  type: reference
---

# Embedding Strategies (GoF Strategy)

nexus-ingest must turn chunks into vectors, but *which* model is a deployment
choice: a local model for offline dev, a hosted API in production, a stub in
tests. The **Strategy** pattern (GoF) captures this: a single interface, many
interchangeable implementations, selected at runtime. The pipeline depends on
the protocol, never on a concrete embedder.

## The protocol — two methods, not one

Retrieval is **asymmetric**: the text of a stored document and the text of a
search query are embedded differently so a query vector lands near the
documents that answer it. The protocol makes this explicit with two methods
plus a `dimension` property.

```python
# app/embeddings/base.py
from typing import Protocol, Sequence

class EmbeddingStrategy(Protocol):
    @property
    def dimension(self) -> int: ...                       # always 1024 in Nexus

    def embed_documents(self, texts: Sequence[str]) -> list[list[float]]: ...
    def embed_query(self, text: str) -> list[float]: ...
```

| Method            | Used at           | Prompt / input_type |
|-------------------|-------------------|---------------------|
| `embed_documents` | ingest / upsert   | `"document"`        |
| `embed_query`     | search time       | `"query"`           |

Collapsing these into one method silently drops recall — the model never gets
the cue that it is encoding a question.

## The three adapters

```python
# app/embeddings/local.py — sentence-transformers, mxbai (1024 native, Matryoshka)
class LocalEmbedder:
    def __init__(self, model="mixedbread-ai/mxbai-embed-large-v1"):
        self._name, self._model = model, None
    def _ensure(self):
        if self._model is None:
            from sentence_transformers import SentenceTransformer  # lazy: pulls torch
            self._model = SentenceTransformer(self._name)
    @property
    def dimension(self): return 1024
    def embed_documents(self, texts):
        self._ensure()
        return self._model.encode(list(texts), normalize_embeddings=True).tolist()
    def embed_query(self, text):
        self._ensure()
        prompt = "Represent this sentence for searching relevant passages: "
        return self._model.encode([prompt + text], normalize_embeddings=True)[0].tolist()
```

```python
# app/embeddings/voyage.py — hosted API, input_type is the asymmetry knob
class VoyageEmbedder:
    def __init__(self, model="voyage-3-large"):
        self._name, self._client = model, None
    def _ensure(self):
        if self._client is None:
            import voyageai                       # lazy
            self._client = voyageai.Client()
    @property
    def dimension(self): return 1024
    def _embed(self, texts, input_type):
        self._ensure()
        r = self._client.embed(list(texts), model=self._name,
                               input_type=input_type, output_dimension=1024)
        return r.embeddings
    def embed_documents(self, texts): return self._embed(texts, "document")
    def embed_query(self, text):      return self._embed([text], "query")[0]
```

```python
# app/embeddings/fake.py — deterministic hash, no network, no torch
import hashlib, struct
class FakeEmbedder:
    @property
    def dimension(self): return 1024
    def _vec(self, text):
        out, i = [], 0
        while len(out) < 1024:
            h = hashlib.sha256(f"{i}:{text}".encode()).digest()
            out += [v / 255.0 for v in h]; i += 1
        return out[:1024]
    def embed_documents(self, texts): return [self._vec(t) for t in texts]
    def embed_query(self, text):      return self._vec(text)
```

`FakeEmbedder` is the workhorse of the test suite: same input → same vector,
so similarity assertions are stable and CI needs no model download or API key.

## The factory

```python
# app/embeddings/factory.py
import os
def build_embedder() -> "EmbeddingStrategy":
    provider = os.getenv("NEXUS_EMBEDDING_PROVIDER", "fake").lower()
    if provider == "local":
        from .local import LocalEmbedder;   return LocalEmbedder()   # torch loads here
    if provider == "voyage":
        from .voyage import VoyageEmbedder; return VoyageEmbedder()  # voyageai loads here
    if provider == "fake":
        from .fake import FakeEmbedder;     return FakeEmbedder()
    raise ValueError(f"unknown NEXUS_EMBEDDING_PROVIDER: {provider!r}")
```

Heavy libraries are imported **inside the chosen branch**, not at module top.
A `fake`/`voyage` deployment never imports `torch`; a `local` one never needs
the Voyage SDK. The factory is the only place that knows concrete classes.

## Standardize on 1024 — the swap invariant

Every adapter reports `dimension == 1024`, matching the Phase 9 schema column
`document_chunks(embedding vector(1024))`. Because mxbai is natively 1024 (and
Matryoshka-truncatable) and Voyage takes `output_dimension=1024`, you can flip
`NEXUS_EMBEDDING_PROVIDER` **without a re-embed migration** of existing rows —
the vectors still fit the column and the `<=>` cosine search still runs. Mixing
dimensions would force a full re-embed; the shared 1024 contract is what makes
the strategies truly interchangeable.

## In Nexus

This is **Rule 7 — program to an interface, not an implementation** made
concrete. The ingest upsert (idempotent on `(document_id, chunk_index)`) calls
`embed_documents`; the search path calls `embed_query` then
`ORDER BY embedding <=> $1 LIMIT k`. Neither knows or cares which embedder is
wired — only the factory and an env var decide.

## References

- Orchestrator: [search_orchestrator.md](../../fundamentals/search/search_orchestrator.md) · `[[search_orchestrator]]`
- Chunking & embeddings: [embeddings.md](../../fundamentals/search/embeddings.md) · `[[embeddings]]`
- Lexical vs semantic: [lexical_vs_semantic.md](../../fundamentals/search/lexical_vs_semantic.md) · `[[lexical_vs_semantic]]`
- ANN indexes: [vector_indexes_ann.md](../../fundamentals/search/vector_indexes_ann.md) · `[[vector_indexes_ann]]`
- pgvector internals: [pgvector.md](../../fundamentals/search/pgvector.md) · `[[pgvector]]`
- Hybrid search: [hybrid_search.md](../../fundamentals/search/hybrid_search.md) · `[[hybrid_search]]`
- Lesson log: [lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`
