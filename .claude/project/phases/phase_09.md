---
name: phase_09
description: Phase 9 tracker — search & vectors (pgvector, embeddings).
agent:
  role: progress-tracker
  tier: nano
  weight: soft
  triggers:
    - reviewing or planning Phase 9
metadata:
  type: reference
---

# Phase 9 — Search & Vectors

| Field | Value |
|---|---|
| Version | `v0.9.0` ✅ |
| Lesson | 10 |
| Topics | search-engines, elasticsearch, vectors, embeddings |
| Status | ✅ shipped — semantic search proven live with the local model |

## Deliverables
- [x] pgvector schema — `document_chunks(embedding vector(1024))` + HNSW cosine (V6)
- [x] `documents.content` stored (V5) so there is text to embed (claim-check)
- [x] Chunking + embedding **strategies** (Strategy pattern): Local/Voyage/Fake + chunker
- [x] Consumer pipeline: fetch → chunk → embed → idempotent upsert (`Ingestor`)
- [x] Vector similarity search endpoint (`GET /v1/search`, HNSW cosine)
- [ ] Full-text baseline + hybrid (RRF) fusion — deferred follow-up

## Proof
3 topic docs embedded by `mxbai`; word-different queries ranked the right topic
first (docker 0.628 / auth 0.693 / redis 0.547). See
[lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`.

## Notes
- Embeddings standardized on **1024 dims** → providers swap with no re-embed.
- Fixed a latent Flyway fat-jar bug (`mergeServiceFiles()`); see
  [decisions.md](../decisions.md) · `[[decisions]]` #18.

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
