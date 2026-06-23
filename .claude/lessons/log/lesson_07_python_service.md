---
name: lesson_07_python_service
description: Lesson 7 log — Python, FastAPI, nexus-ingest scaffold.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 7 covered
    - recapping FastAPI/nexus-ingest before a session
metadata:
  type: reference
---

# Lesson 7 — Python Service (Phase 6)

| Field | Value |
|---|---|
| Phase | 6 — Python Service |
| Roadmap topics | python, web framework (FastAPI), package management |
| Deliverable | `nexus-ingest` FastAPI scaffold — `/v1/health` (parity with nexus-api) |
| Milestone | `v0.6.0` |
| Status | ✅ mastery pass (scaffold) |

## Concepts taught
Polyglot backend (Kotlin gateway + Python AI services, all backend); FastAPI ≈
Ktor mapping (create_app, APIRouter prefix, Pydantic models, Uvicorn/ASGI);
Pydantic at the HTTP edge; FastAPI auto-generates OpenAPI; uv for Python/venv.

## Exercises (recap)
| Q | Topic | Verdict |
|---|---|---|
| R1 | cache-aside on miss (DB → cache → return) | ✅ **gap closed** |
| R2 | denylist TTL = remaining life | 🟡 sharpened (self-cleaning) |
| R3 | hints need a static checker (mypy), not Protocols | ❌ corrected |

## Built (verified)
- `nexus-ingest/`: `pyproject.toml` (3.13; FastAPI, Uvicorn, Pydantic, pydantic-settings)
- `app/main.py` (create_app), `config.py` (env Settings), `errors.py` (single
  `{error,message}` shape), `routes/v1/health.py`
- `GET /v1/health` → identical shape to nexus-api; OpenAPI auto at `/openapi.json`
- `pytest` (TestClient) ✅, `mypy --strict` ✅, `ruff` ✅; curl-verified
- Toolchain: brew python@3.13 had a broken pyexpat → switched to **uv** (managed CPython)

## Gaps to revisit
- R3 (type-checker vs Protocols) — re-surface next recap.
- Next: ingestion endpoints + Kafka consumer (Phase 7).

## References
- [lesson_06_testing.md](lesson_06_testing.md) · `[[lesson_06_testing]]`
- [nexus_ingest_orchestrator.md](../../services/nexus-ingest/nexus_ingest_orchestrator.md) · `[[nexus_ingest_orchestrator]]`
- [python_language_orchestrator.md](../../languages/python/python_language_orchestrator.md) · `[[python_language_orchestrator]]`
