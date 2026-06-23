---
name: phase_05
description: Phase 5 tracker — testing & CI/CD.
agent:
  role: progress-tracker
  tier: nano
  weight: soft
  triggers:
    - reviewing or planning Phase 5
metadata:
  type: reference
---

# Phase 5 — Testing & CI/CD

| Field | Value |
|---|---|
| Version | `v0.5.0` 🔄 in progress |
| Lesson | 6 |
| Topics | testing, unit-testing, integration-testing, functional-testing, ci--cd |
| Status | 🔄 unit tests + CI shipped; coverage reporting pending |

## Deliverables
- [x] Pure unit test (`PasswordHasherTest`) — no fake, no Docker
- [x] Unit test with a fake (`TokenDenylistTest` + `FakeCache`)
- [x] Integration tests against real Postgres + Redis (`Auth`/`HealthRouteTest`)
- [x] GitHub Actions CI (`.github/workflows/ci.yml`) — build + test, Postgres + Redis services
- [ ] Coverage reporting (JaCoCo) — deferred

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [testing_strategy.md](../../services/nexus-api/testing/testing_strategy.md) · `[[testing_strategy]]`
