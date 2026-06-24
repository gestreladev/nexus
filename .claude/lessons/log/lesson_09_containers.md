---
name: lesson_09_containers
description: Lesson 9 log — containerize the whole system; one `docker compose up`.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 9 covered
    - recapping containers/compose/architecture before a session
metadata:
  type: reference
---

# Lesson 9 — Containers & Architecture (Phase 8)

| Field | Value |
|---|---|
| Phase | 8 — Containers & Architecture |
| Roadmap topics | containers, twelve-factor-apps, architectural-patterns, monolith, microservices, soa, serverless, service-mesh, circuit-breaker |
| Deliverable | Whole system runs from one `docker compose up` (5 services healthy) |
| Milestone | `v0.8.0` |
| Status | ✅ mastery pass |

## Concepts taught
- **Multi-stage build** — a fat build stage (JDK+Gradle / `uv`) compiles, a slim
  runtime stage (JRE / `python:3.13-slim`) ships only the artifact → smaller
  image **and** smaller attack surface. Both images run as a **non-root** user.
- **12-factor config** — config lives in the environment, not the image. Same
  image, different env per deploy. `DB_URL`/`REDIS_URL`/`KAFKA_BOOTSTRAP_SERVERS`/
  `JWT_SECRET` are injected; code reads `System.getenv(...) ?: yaml-default`.
- **Container networking flip** — host clients use `localhost:<published>`;
  in-network containers use **service-name DNS** + container port
  (`postgres:5432`, `redis:6379`, `kafka:29092`).
- **Kafka dual-listener** — a broker *advertises* an address back to clients, so
  one address can't serve both worlds. EXTERNAL advertises `localhost:9092`
  (host), INTERNAL advertises `kafka:29092` (containers); `INTER_BROKER` uses
  INTERNAL. This is the lesson's signature gotcha.
- **Readiness vs start** — `depends_on: condition: service_healthy` + a
  healthcheck so `nexus-api` never boots before Postgres/Kafka actually accept
  connections.
- **Architectural patterns (concepts)** — monolith vs microservices vs SOA vs
  serverless; service mesh (sidecar networking); circuit breaker (fail fast when
  a dependency is down).

## Exercises (recap, from session start)
| Q | Topic | Verdict |
|---|---|---|
| R1 | idempotent consumer under at-least-once delivery | 🔁 re-taught |
| R2 | mypy strict config (`[tool.mypy] python_version="3.13" strict=true`) | ✅ |
| R3 | multi-stage = smaller image **+** smaller attack surface | 🔁 re-taught |

## Built (verified end-to-end)
- `nexus-api/Dockerfile` — multi-stage (Temurin 17 JDK build → 17 JRE runtime),
  non-root `appuser` (uid 10001), `buildFatJar` → `app.jar`; `+ .dockerignore`.
- `nexus-ingest/Dockerfile` — multi-stage (`uv` venv → `python:3.13-slim`),
  non-root; `+ .dockerignore`.
- `docker-compose.yml` — Kafka **dual-listener** (EXTERNAL/INTERNAL); added
  `nexus-api` + `nexus-ingest` services with `depends_on: service_healthy`,
  env-var config, and curl healthchecks.
- `ModulePipeline.database()` — `DB_URL`/`DB_USER`/`DB_PASSWORD` env overrides.
- **Proven:** all 5 containers `Up (healthy)`. `POST /v1/documents` on
  `localhost:8080` → nexus-api `Published … document.uploaded-0@0` → nexus-ingest
  `consumed … (partition 0 offset 0)` — the event crossed the network on
  `kafka:29092`, container-to-container.

## Gaps to revisit
- `/v1/health` `version` is a manual `application.yaml` value (`nexus.version`,
  bumped to `0.8.0` here) duplicated from Gradle `version` — not yet derived from
  a single build-metadata source. (It reads from config, not a Kotlin literal.)
- Reverse proxy (nginx/Caddy) + circuit-breaker as code are concepts only here;
  deferred to a later infra phase.

## References
- [lesson_08_messaging.md](lesson_08_messaging.md) · `[[lesson_08_messaging]]`
- [docker_orchestrator.md](../../infra/docker/docker_orchestrator.md) · `[[docker_orchestrator]]`
- [phase_08.md](../../project/phases/phase_08.md) · `[[phase_08]]`
