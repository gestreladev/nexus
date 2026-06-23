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
| Released | `v0.3.0` (Auth & Security) ✅ tagged |
| Last lesson | **Lesson 6 — Testing & CI/CD** ✅ (see log) |
| Next lesson | **Lesson 7 — Python Service** (Phase 6 → `v0.6.0`) |
| Active branch | `feat/phase-5-testing-ci` (unit tests + GitHub Actions, suite green) |
| Open gaps | R1/R2 caching recall (re-surface next recap); coverage reporting deferred |

## Done
- ✅ Phase 0 — repo, structure, conventions
- ✅ Phase 1 — `nexus-api` scaffold, `/v1/health`, ModulePipeline DSL (`v0.1.0`)
- ✅ Phase 2 — PostgreSQL data layer, Flyway, Exposed, partial index (`v0.2.0`)
- ✅ Phase 3 — auth layer: bcrypt + JWT, register/login/me, `authentication()`
  pipeline stage, 5 integration tests (`v0.3.0`)
- 🔄 Phase 4 — Redis cache-aside (`/me`) + JWT denylist (`logout`); Redis in
  compose; Lettuce client (`v0.4.0`, HTTP cache headers pending)
- 🔄 Phase 5 — unit tests (pure + with fake) + `FakeCache`; GitHub Actions CI
  with Postgres + Redis services (`v0.5.0`, coverage reporting pending)
- ✅ Tooling — `.claude` vault (fundamentals, languages, lessons, project, infra,
  security, caching), GoF Kotlin tree, PR templates, labels, milestones

## In progress
- Phase 4: HTTP caching headers deferred until public read endpoints exist
- Phase 5: JaCoCo coverage reporting deferred

## Next action
Start **Lesson 7 — Python Service** (Phase 6): build the Python full GoF tree
first (new-language rule), then scaffold `nexus-ingest` in FastAPI.
Branch `feat/phase-6-python`, milestone `v0.6.0`.

## Backlog (deferred)
- Python GoF tree → build when `nexus-ingest` starts (Phase 6 / v0.6.0).

## References
- [project_develop_orchestrator.md](project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
- [phases/phase_03.md](phases/phase_03.md) · `[[phase_03]]`
- [lesson_orchestrator.md](../lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]`
