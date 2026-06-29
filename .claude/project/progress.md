---
name: progress
description: Live development status and next action — the resume pointer for Nexus.
agent:
  role: progress-tracker
  tier: standard
  weight: soft
  triggers:
    - resuming work or learning
    - checking what is done and what is next
metadata:
  type: reference
---

# Progress — Nexus

> **Resume here.** This is the live status. Updated at the close of every lesson
> / dev session (see session_protocol).

## Current state
| Field | Value |
|---|---|
| Released | `v0.9.0` tagged; `v0.4.0`–`v0.9.0` milestones closed |
| Current lesson | **Lesson 11 — Observability** 🔄 (Phase 10, Session A ✅ tracing live) |
| Next | **Session B** — metrics (RED) + structured logs + log↔trace correlation |
| Active branch | `feat/phase-10-observability` (uncommitted Session A) |
| Open gaps | R2 retrieval-asymmetry + E1 traces-not-logs (re-surface); full-text/hybrid deferred; `/v1/health` version not wired to build metadata |

## Done
- ✅ Phase 0 — repo, structure, conventions
- ✅ Phase 1 — `nexus-api` scaffold, `/v1/health`, ModulePipeline DSL (`v0.1.0`)
- ✅ Phase 2 — PostgreSQL data layer, Flyway, Exposed, partial index (`v0.2.0`)
- ✅ Phase 3 — auth layer: bcrypt + JWT, register/login/me, `authentication()`
  pipeline stage, 5 integration tests (`v0.3.0`)
- ✅ Phase 4 — Redis cache-aside (`/me`) + JWT denylist (`logout`); Lettuce
  client (`v0.4.0`, PR #13) — HTTP cache headers still deferred
- ✅ Phase 5 — unit tests (pure + with fake) + `FakeCache`; GitHub Actions CI
  with Postgres + Redis services (`v0.5.0`, PR #15) — coverage reporting deferred
- ✅ Phase 6 — `nexus-ingest` FastAPI scaffold (`/v1/health`, Python 3.13, uv,
  mypy strict); Python GoF tree + language reference (`v0.6.0`, PR #18)
- ✅ Phase 7 — Kafka event flow: `nexus-api` produces → `nexus-ingest` consumes
  (`v0.7.0`, PR #19)
- ✅ Phase 8 — whole system containerized; one `docker compose up` runs 5 healthy
  services; multi-stage images, 12-factor config, Kafka dual-listener (`v0.8.0`)
- ✅ Phase 9 — vector search: `document_chunks` (pgvector + HNSW), `nexus-ingest`
  pipeline (chunk→embed→upsert) + `GET /v1/search`; embedding Strategy
  (local/Voyage/fake); semantic search proven live (`v0.9.0`, tagged + released)
- 🔄 Phase 10 — observability **Session A**: OTel tracing across both services
  (Java agent + Python SDK), traceparent across Kafka, Grafana LGTM backend;
  one upload verified as a single 14-span trace (`v0.10.0`)
- ✅ Tooling — `.claude` vault (fundamentals, languages, lessons, project, infra,
  security, caching), GoF Kotlin + Python trees, PR-walkthrough standard, labels, milestones

## In progress
- Phase 4: HTTP caching headers deferred until public read endpoints exist
- Phase 5: JaCoCo coverage reporting deferred

## Next action
**Lesson 11 — Session B** (Phase 10): flip `OTEL_METRICS_EXPORTER=otlp`, build a
RED Grafana dashboard (rate/errors/p99), structured JSON logs carrying `trace_id`,
and log↔trace correlation in Loki/Grafana. Then milestone `v0.10.0` merge.

## Backlog (deferred)
- Reverse proxy (nginx/Caddy) + circuit-breaker as code (Phase 8 concepts only).
- `/v1/health` version: derive from a single build-metadata source (drop the
  application.yaml ↔ Gradle duplication).
- Container hardening follow-ups from the Phase 8 audit: image-level resource
  tuning beyond mem_limit, base-image digest pinning, JDK 17→21 currency.

## References
- [project_develop_orchestrator.md](project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
- [phases/phase_03.md](phases/phase_03.md) · `[[phase_03]]`
- [lesson_orchestrator.md](../lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]`
