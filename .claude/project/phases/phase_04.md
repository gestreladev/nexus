---
name: phase_04
description: Phase 4 tracker — caching (Redis, Memcached, NoSQL).
agent:
  role: progress-tracker
  tier: nano
  weight: soft
  triggers:
    - reviewing or planning Phase 4
metadata:
  type: reference
---

# Phase 4 — Caching

| Field | Value |
|---|---|
| Version | `v0.4.0` ⏳ |
| Lesson | 5 |
| Topics | caching, redis, memcached, nosql-databases, http-caching |
| Status | ⏳ planned |

## Planned deliverables
- [ ] Redis in Docker Compose
- [ ] Cache layer in `nexus-api` (decorator over a repository)
- [ ] Cache-aside pattern + invalidation strategy
- [ ] HTTP caching headers where appropriate

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [caching_orchestrator.md](../../fundamentals/caching/caching_orchestrator.md) · `[[caching_orchestrator]]`
- [redis_orchestrator.md](../../infra/redis/redis_orchestrator.md) · `[[redis_orchestrator]]`
- [revocation_and_denylist.md](../../security/jwt/revocation_and_denylist.md) · `[[revocation_and_denylist]]`
