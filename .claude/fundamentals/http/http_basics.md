---
name: http_basics
description: HTTP methods, status codes, and headers.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - choosing an HTTP method or status code
    - understanding request/response headers
metadata:
  type: reference
---

# HTTP Basics

HTTP is a **stateless, text-based** request/response protocol (binary in
HTTP/2). Every request is independent — the server remembers nothing between
them (hence tokens; see [idempotency_and_statelessness.md](idempotency_and_statelessness.md) · `[[idempotency_and_statelessness]]`).

## Methods
| Method | Meaning |
|---|---|
| `GET` | read |
| `POST` | create |
| `PUT` | full replace |
| `PATCH` | partial update |
| `DELETE` | remove |

## Status code families
| Range | Meaning |
|---|---|
| `2xx` | success |
| `3xx` | redirect |
| `4xx` | client error (their fault) |
| `5xx` | server error (our fault) |

Use them honestly — never `200 OK` with `{"error": ...}` in the body.

## Headers
Metadata on every message. The ones you touch daily:
- `Content-Type` — body format (`application/json`)
- `Authorization` — credentials (`Bearer <token>`)
- `Accept` — what the client wants back
- `Cache-Control` — caching policy

## Why it matters in Nexus
This is the contract `nexus-api` exposes. The project's concrete choices
(which code for which case, the single error shape) live in
[conventions.md](../../services/nexus-api/api/conventions.md) · `[[conventions]]`.

## References
- [http_orchestrator.md](http_orchestrator.md) · `[[http_orchestrator]]`
- [lesson_01_foundations.md](../../lessons/log/lesson_01_foundations.md) · `[[lesson_01_foundations]]`
