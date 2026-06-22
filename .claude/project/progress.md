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
| Released | `v0.2.0` (Data Layer) |
| Last lesson | **Lesson 3 — Data Layer** ✅ (see log) |
| Next lesson | **Lesson 4 — Auth & Security** (Phase 3 → `v0.3.0`) |
| Active branch | `main` (clean) |
| Open gaps | 3NF normalization (re-surface in Lesson 4 Recap) |

## Done
- ✅ Phase 0 — repo, structure, conventions
- ✅ Phase 1 — `nexus-api` scaffold, `/v1/health`, ModulePipeline DSL (`v0.1.0`)
- ✅ Phase 2 — PostgreSQL data layer, Flyway, Exposed, partial index (`v0.2.0`)
- ✅ Tooling — `.claude` vault structure, GoF Kotlin tree, PR templates, labels,
  milestones

## In progress
- _none_ (between phases)

## Next action
Start **Lesson 4 — Auth & Security**: JWT, OAuth2, OWASP Top 10, TLS, bcrypt.
Deliverable: auth layer in `nexus-api` (register/login, protected routes).
Branch `feat/phase-3-auth`, milestone `v0.3.0`.

## Backlog (deferred)
- Python GoF tree → build when `nexus-ingest` starts (Phase 6 / v0.6.0).

## References
- [project_develop_orchestrator.md](project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
- [phases/phase_03.md](phases/phase_03.md) · `[[phase_03]]`
- [lesson_orchestrator.md](../lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]`
