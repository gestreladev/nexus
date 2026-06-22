---
name: lesson_01_foundations
description: Lesson 1 log — Internet, HTTP, DNS, Git, GitHub + repo setup.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 1 covered
    - recapping foundations before a session
metadata:
  type: reference
---

# Lesson 1 — Foundations (Phase 0)

| Field | Value |
|---|---|
| Phase | 0 — Setup |
| Roadmap topics | how-internet-works, http, https, dns, version-control, git, github |
| Deliverable | `gestreladev/nexus` repo + monorepo structure, branch protection |
| Status | ✅ mastery pass |

## Concepts taught
Request lifecycle (DNS → TCP → TLS → HTTP), HTTP methods/status/headers,
idempotency, statelessness, HTTPS = HTTP + TLS, DNS record types.

## Exercises
| Q | Topic | Verdict |
|---|---|---|
| 1 | 200-with-error-body is wrong; should be 404 + no `success` field | ✅ |
| 2 | Post-login state lives in a client-held token | ✅ |
| 3 | TCP connection reuse via HTTP Keep-Alive | ❌ → explained |

## Gaps to revisit
- **HTTP Keep-Alive / connection pooling** was new. (Re-touched in Lesson 3 via
  HikariCP connection pooling — now solid.)

## Notes
Established commit convention and branch model. Set up dual GitHub accounts
(SSH host alias `github-gestreladev`).

## References
- [lesson_orchestrator.md](../lesson_orchestrator.md) · `[[lesson_orchestrator]]`
- [lesson_02_first_service.md](lesson_02_first_service.md) · `[[lesson_02_first_service]]`
