---
name: idempotency_and_statelessness
description: Idempotency of HTTP methods and where state lives in a stateless protocol.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - reasoning about safe retries
    - deciding where session/auth state lives
metadata:
  type: reference
---

# Idempotency & Statelessness

## Idempotency
An operation is **idempotent** if calling it N times has the same effect as
calling it once.

| Method | Idempotent? | Why it matters |
|---|---|---|
| `GET` | ✅ | safe to retry/cache |
| `PUT` | ✅ | full replace → same result |
| `DELETE` | ✅ | deleting twice = still deleted |
| `PATCH` | usually ✅ | depends on the patch |
| `POST` | ❌ | creates a new resource each time |

This drives **retry safety**: a client/proxy can safely retry a `GET` or `PUT`
on a network blip, but retrying a `POST` may create duplicates. For critical
`POST`s, an *idempotency key* makes them safe.

## Statelessness
The server keeps **no per-client state** between requests. Consequences:
- State lives in the **client** (a token) or the **database**, never in server
  memory between calls.
- This is what makes horizontal scaling trivial — any instance can serve any
  request, because nothing is "stuck" on one node.

```
login → server issues a token → client stores it
every later request → Authorization: Bearer <token> → server validates
```

## Why it matters in Nexus
Auth (Lesson 4) is built on this: no server-side session: a signed JWT carries
identity, validated per request. Lets us run many `nexus-api` instances behind a
load balancer with no shared session store.

## References
- [http_orchestrator.md](http_orchestrator.md) · `[[http_orchestrator]]`
- [http_basics.md](http_basics.md) · `[[http_basics]]`
