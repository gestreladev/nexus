---
name: lesson_06_testing
description: Lesson 6 log — test pyramid, unit vs integration, fakes, CI/CD.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 6 covered
    - recapping testing/CI before a session
metadata:
  type: reference
---

# Lesson 6 — Testing & CI/CD (Phase 5)

| Field | Value |
|---|---|
| Phase | 5 — Testing & CI/CD |
| Roadmap topics | testing, unit-testing, integration-testing, functional-testing, ci--cd |
| Deliverable | Unit tests (pure + with fake) + GitHub Actions CI with services |
| Milestone | `v0.5.0` |
| Status | ✅ mastery pass |

## Concepts taught
Test pyramid (many unit / some integration / few E2E); compiler catches
syntax/types, unit tests catch *logic*; **the dependency determines the test** —
no deps → pure unit; dep behind an interface → unit with a **fake**; fakes vs
mocks; CI/CD with service containers.

## Exercises (recap + new)
| Q | Topic | Verdict |
|---|---|---|
| R1 | cache-aside on miss (load→populate→return) | ❌→retaught |
| R2 | denylist TTL = remaining life (self-cleans) | ❌→retaught |
| R3 | bcrypt slow-on-purpose (work factor) | 🟡 partial |
| E1 | both current tests are integration | ✅ |
| E2 | cost of zero unit tests (logic, not syntax) | 🟡 corrected |
| E3 | PasswordHasher = pure unit; TokenDenylist = needs fake | ❌→reversed, corrected |

## Built (verified)
- `FakeCache` (in-memory `Cache`) — the payoff of the Lesson-5 interface
- `PasswordHasherTest` — **pure** unit (no fake, no Docker)
- `TokenDenylistTest` — unit **with FakeCache** (TTL math, ttl>0 guard)
- `.github/workflows/ci.yml` — build + test on PR/push, Postgres + Redis services
- **Proven:** unit tests pass with Docker fully stopped; full suite green with services up

## Gaps to revisit
- R1 + R2 (caching) — re-surface next recap (immediate-recall misses).
- Coverage reporting (JaCoCo) — deferred.

## References
- [lesson_05_caching.md](lesson_05_caching.md) · `[[lesson_05_caching]]`
- [testing_strategy.md](../../services/nexus-api/testing/testing_strategy.md) · `[[testing_strategy]]`
