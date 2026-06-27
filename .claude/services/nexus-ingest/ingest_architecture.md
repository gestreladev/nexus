---
name: ingest_architecture
description: nexus-ingest app layout and the parity map with nexus-api.
agent:
  role: fastapi-specialist
  tier: standard
  weight: soft
  triggers:
    - deciding where new nexus-ingest code goes
    - mapping a Ktor concept to FastAPI
metadata:
  type: reference
---

# nexus-ingest — Architecture

## Layout
```
nexus-ingest/
├── pyproject.toml        deps + tooling (uv, ruff, mypy, pytest); embed-local/voyage extras
├── Dockerfile            multi-stage; bakes the local model into HF_HOME
├── app/
│   ├── main.py           create_app() + lifespan wires db/embedder/consumer
│   ├── config.py         env-driven Settings (pydantic-settings)
│   ├── errors.py         ErrorResponse + handlers (single error shape)
│   ├── db.py             asyncpg + pgvector: fetch content, upsert chunks, search
│   ├── ingest.py         Ingestor: fetch → chunk → embed → idempotent upsert
│   ├── embeddings/       EmbeddingStrategy + Local/Voyage/Fake + factory
│   ├── chunking/         ChunkingStrategy + CharacterChunker
│   ├── messaging/        DocumentConsumer (drives the Ingestor)
│   └── routes/v1/        versioned routers (health.py, search.py …)
└── tests/                pytest (unit + a live-Postgres integration test)
```

## Parity map (Ktor → FastAPI)
| Concept | nexus-api (Kotlin) | nexus-ingest (Python) |
|---|---|---|
| App wiring | `Application.module()` | `create_app()` → `FastAPI` |
| Version prefix | `route("/v1")` | `include_router(r, prefix="/v1")` |
| Handler | `get("/health") { }` | `@router.get("/health")` |
| Request/response | `@Serializable data class` | Pydantic `BaseModel` |
| Errors | `StatusPages` → `ErrorResponse` | exception handlers → same `{error,message}` |
| Config | `application.yaml` + env | `Settings` (env `NEXUS_*`) |
| Engine | Netty | Uvicorn (ASGI) |
| OpenAPI | generate from routes | **auto** at `/openapi.json` + `/docs` |

## Conventions (shared with nexus-api)
- All routes under `/v1/`.
- One error shape: `{ "error": "CODE", "message": "..." }` — never leak internals.
- Secrets/config from the environment; no hardcoded secrets.
- `camelCase`? No — Python/JSON here stays as defined by the Pydantic models;
  keep field names consistent within the service.

## Where to put new code
| What | Where |
|---|---|
| New endpoint | `app/routes/v1/<resource>.py` (an `APIRouter`) |
| Request/response model | a Pydantic `BaseModel` beside the route |
| New setting | `app/config.py` |
| New error type | raise `AppError(status, code, msg)` (in `app/errors.py`) |

## References
- [nexus_ingest_orchestrator.md](nexus_ingest_orchestrator.md) · `[[nexus_ingest_orchestrator]]`
- [layers.md](../nexus-api/architecture/layers.md) · `[[layers]]` — the Kotlin layering it mirrors
