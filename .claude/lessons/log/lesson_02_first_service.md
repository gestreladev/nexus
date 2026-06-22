---
name: lesson_02_first_service
description: Lesson 2 log — web servers, REST, OpenAPI + nexus-api scaffold.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 2 covered
    - recapping REST/first-service before a session
metadata:
  type: reference
---

# Lesson 2 — First Service (Phase 1)

| Field | Value |
|---|---|
| Phase | 1 — First Service |
| Roadmap topics | web-servers, nginx (concept), rest, json-apis, open-api-specs |
| Deliverable | `nexus-api` Ktor scaffold, `/v1/health`, ModulePipeline DSL |
| Milestone | `v0.1.0` ✅ |
| Status | ✅ mastery pass |

## Concepts taught
App server vs reverse proxy, REST resource modeling (nouns/methods), status-code
contract, API versioning (additive-safe vs breaking), OpenAPI. Plus depth the
learner requested: Chain of Responsibility + `@DslMarker` (the pipeline).

## Exercises
| Q | Topic | Verdict |
|---|---|---|
| 1 | Renaming a field is breaking → version; additive is safe | 🟡 → refined |
| 2 | Missing field → 400 Bad Request | ✅ |
| 3 | 401 (unauthenticated) vs 403 (forbidden) | ✅ |

## Gaps to revisit
- Versioning nuance (additive vs breaking) — refined in-lesson; confirm at v0.3.0.

## Notes
Built `ModulePipeline` as a Chain-of-Responsibility via a `@DslMarker` DSL with
fail-fast stage ordering. Pinned Gradle 8.13 (Ktor shadow-plugin compat).

## References
- [lesson_01_foundations.md](lesson_01_foundations.md) · `[[lesson_01_foundations]]`
- [lesson_03_database.md](lesson_03_database.md) · `[[lesson_03_database]]`
