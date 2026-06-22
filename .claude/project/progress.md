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
| Released | `v0.2.0` (Data Layer); `v0.3.0` ready to tag (auth shipped) |
| Last lesson | **Lesson 4 — Auth & Security** ✅ (see log) |
| Next lesson | **Lesson 5 — Caching** (Phase 4 → `v0.4.0`) |
| Active branch | `feat/phase-3-auth` (auth layer, tests green) |
| Open gaps | E5 JWT staleness/revocation — taught; confirm at Lesson 5 recap |

## Done
- ✅ Phase 0 — repo, structure, conventions
- ✅ Phase 1 — `nexus-api` scaffold, `/v1/health`, ModulePipeline DSL (`v0.1.0`)
- ✅ Phase 2 — PostgreSQL data layer, Flyway, Exposed, partial index (`v0.2.0`)
- ✅ Phase 3 — auth layer: bcrypt + JWT, register/login/me, `authentication()`
  pipeline stage, 5 integration tests (`v0.3.0`)
- ✅ Tooling — `.claude` vault (fundamentals, languages, lessons, project), GoF
  Kotlin tree, PR templates, labels, milestones

## In progress
- _none_ (Phase 3 complete; OAuth2 covered conceptually, full provider deferred)

## Next action
Start **Lesson 5 — Caching** (Phase 4): Redis, cache-aside, invalidation,
HTTP caching. Deliverable: a caching decorator over a repository in `nexus-api`.
Branch `feat/phase-4-caching`, milestone `v0.4.0`.

## Backlog (deferred)
- Python GoF tree → build when `nexus-ingest` starts (Phase 6 / v0.6.0).

## References
- [project_develop_orchestrator.md](project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
- [phases/phase_03.md](phases/phase_03.md) · `[[phase_03]]`
- [lesson_orchestrator.md](../lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]`
