---
name: chunking
description: How nexus-ingest splits documents into overlapping chunks before embedding.
agent:
  role: fastapi-specialist
  tier: standard
  weight: medium
  triggers:
    - tuning chunk size or overlap for retrieval quality
    - swapping the character splitter for a sentence/recursive one
metadata:
  type: reference
---

# Chunking Strategy

Embedding a whole document into one vector blurs everything together: a
1024-dim vector for 50 paragraphs loses the local detail a query needs. So
nexus-ingest **chunks** each document into smaller passages, embeds each
chunk independently, and stores them as rows in `document_chunks`. Retrieval
then matches against passages, not whole files.

## The ChunkingStrategy Protocol

Chunking is behind a structural `Protocol` so the splitter is swappable
without touching the ingest pipeline:

```python
from typing import Protocol

class ChunkingStrategy(Protocol):
    def chunk(self, text: str) -> list[str]: ...
```

Any object with a `chunk(text) -> list[str]` method satisfies it — no base
class, no registration. The pipeline depends on the Protocol; the concrete
splitter is injected.

## Default: CharacterChunker(size, overlap)

The default implementation (file `app/chunking/character.py`) is the simplest
useful one: slide a fixed window of `size` characters across the text,
stepping forward by `size - overlap` each time.

```python
class CharacterChunker:
    def __init__(self, size: int, overlap: int) -> None:
        self.size = size
        self.overlap = overlap

    def chunk(self, text: str) -> list[str]:
        step = self.size - self.overlap
        return [text[i : i + self.size]
                for i in range(0, max(len(text), 1), step)]
```

### Why overlap

A hard boundary every `size` characters will eventually cut **through** a
sentence or concept. If "...the **HNSW index avoids a training step**..."
straddles a boundary, neither neighbouring chunk contains the full phrase,
and a query for it embeds poorly against both. Overlap re-includes the tail
of chunk *N* at the head of chunk *N+1*, so any concept narrower than the
overlap survives intact in **at least one** chunk.

| overlap | effect |
|---------|--------|
| `0`     | clean cuts, but boundary-straddling ideas are lost |
| small (e.g. 10–15% of size) | typical: cheap insurance against split concepts |
| large   | more chunks, more duplicate text, more embeddings to store/search |

Overlap is a recall-vs-cost trade: bigger overlap means more rows in
`document_chunks` and more vectors to ANN-search.

## Configuration

Size and overlap are not hard-coded — they come from settings, so you can
tune retrieval without code changes:

| env var              | meaning                          |
|----------------------|----------------------------------|
| `NEXUS_CHUNK_SIZE`   | window length in characters      |
| `NEXUS_CHUNK_OVERLAP`| characters re-shared per boundary|

Constraint: `OVERLAP < SIZE`, else `step <= 0` and the loop never advances.

## Swappable later

Character windows are blunt — they can split mid-word. Because the pipeline
only depends on `ChunkingStrategy`, smarter splitters drop in unchanged:

- **Sentence splitter** — break on sentence boundaries; chunks read cleanly,
  but length varies.
- **Recursive splitter** — try paragraph, then sentence, then word
  boundaries, packing up to `size`; respects structure while staying bounded.

Each just implements `chunk(text) -> list[str]`. Whatever the splitter
produces is handed to the embedder, so chunking and embedding evolve
independently.

## In Nexus

Phase 9 stores chunks in `document_chunks(embedding vector(1024))` with an
**idempotent upsert keyed on `(document_id, chunk_index)`** — `chunk_index`
is the chunk's position in the list returned by `chunk()`. Re-ingesting the
same document with the same strategy overwrites rows in place rather than
duplicating. Each chunk string is embedded (mxbai locally or Voyage, both
1024-dim, so swappable without a re-embed migration) and searched with
`ORDER BY embedding <=> $1 LIMIT k` over an HNSW cosine index.

Chunk size interacts with the embedding model's context window: chunks must
fit what the encoder accepts, or text past the limit is silently dropped.

## References

- Orchestrator: [search_orchestrator.md](../../fundamentals/search/search_orchestrator.md) · `[[search_orchestrator]]`
- How chunks become vectors: [embedding_strategies.md](embedding_strategies.md) · `[[embedding_strategies]]`
- Embedding concepts: `[[embeddings]]` · lexical vs semantic: `[[lexical_vs_semantic]]`
- Vector storage/ops: `[[pgvector]]` · ANN indexes: `[[vector_indexes_ann]]` · `[[hybrid_search]]`
- Lesson log: [lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`
- DB context: [databases_orchestrator.md](../../fundamentals/databases/databases_orchestrator.md) · `[[databases_orchestrator]]` · [indexing.md](../../fundamentals/databases/indexing.md) · `[[indexing]]`
