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
| Version | `v0.4.0` 🔄 in progress |
| Lesson | 5 |
| Topics | caching, redis, memcached, nosql-databases, http-caching |
| Status | 🔄 cache layer + denylist shipped; HTTP headers pending |

## Deliverables
- [x] Redis (`redis:7-alpine`) in Docker Compose (AOF, healthcheck)
- [x] Cache abstraction (`Cache`/`RedisCache`, Lettuce) + `redis()` pipeline stage
- [x] Cache-aside on `GET /v1/auth/me` (`cache:user:{id}`, TTL 300s)
- [x] **JWT denylist** + `POST /v1/auth/logout` (closes Lesson 4 E5)
- [ ] HTTP caching headers (Cache-Control/ETag) — when public read endpoints exist

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [caching_orchestrator.md](../../fundamentals/caching/caching_orchestrator.md) · `[[caching_orchestrator]]`
- [redis_orchestrator.md](../../infra/redis/redis_orchestrator.md) · `[[redis_orchestrator]]`
- [revocation_and_denylist.md](../../security/jwt/revocation_and_denylist.md) · `[[revocation_and_denylist]]`
