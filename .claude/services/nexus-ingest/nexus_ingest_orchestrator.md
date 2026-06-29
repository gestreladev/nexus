---
name: nexus_ingest_orchestrator
description: Routing and metadata for the nexus-ingest service documentation.
agent:
  role: fastapi-specialist
  tier: standard
  weight: medium
  triggers:
    - starting a nexus-ingest task
    - routing a nexus-ingest request to the correct doc
metadata:
  type: orchestrator
---

# nexus-ingest — Service Orchestrator

Document ingestion service (parse → chunk → embed) — the Python/FastAPI **twin**
of `nexus-api`, built for parity (same `/v1` versioning, same `{error, message}`
shape, same `/v1/health`). Python 3.13. Model selection:
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

**Code tasks (Rule 7):** before writing Python, consult
[python_orchestrator.md](../../designpatterns/python/python_orchestrator.md) · `[[python_orchestrator]]`
(GoF) and the language reference
[python_language_orchestrator.md](../../languages/python/python_language_orchestrator.md) · `[[python_language_orchestrator]]`.

---

## Metadata

| Field | Value |
|---|---|
| Path | `nexus-ingest/` |
| Language | Python 3.13 |
| Framework | FastAPI (ASGI) + Uvicorn |
| Models | Pydantic v2 |
| Packaging | `pyproject.toml` + `uv` venv |
| Port | 8081 |
| Container | multi-stage `Dockerfile` (`python:3.13-slim`, non-root, `uv` venv); runs in Compose with `NEXUS_*` 12-factor env + `/v1/health` healthcheck |
| Observability | OTel SDK (`app/telemetry.py`) — FastAPI/aiokafka/asyncpg instrumented; consumer **extracts** `traceparent` from Kafka headers + a manual `embed` span; OTLP to `lgtm:4318` |
| Status | `v0.9.0` embed + vector search · `v0.10.0` traced (Session A) |

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Architecture | App layout + the nexus-api parity map | [ingest_architecture.md](ingest_architecture.md) · `[[ingest_architecture]]` |
| Embedding strategies | Local/Voyage/Fake adapters behind one Strategy; dim 1024 | [embedding_strategies.md](embedding_strategies.md) · `[[embedding_strategies]]` |
| Chunking | ChunkingStrategy + CharacterChunker (size/overlap) | [chunking.md](chunking.md) · `[[chunking]]` |
| Search endpoint | `GET /v1/search` — embed query → vector NN | [search_endpoint.md](search_endpoint.md) · `[[search_endpoint]]` |

---

## References
- [services_orchestrator.md](../services_orchestrator.md) · `[[services_orchestrator]]`
- [nexus_api_orchestrator.md](../nexus-api/nexus_api_orchestrator.md) · `[[nexus_api_orchestrator]]` — the Kotlin twin
