---
name: lesson_05_caching
description: Lesson 5 log — caching, Redis, cache-aside, and the JWT denylist.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 5 covered
    - recapping caching/Redis before a session
metadata:
  type: reference
---

# Lesson 5 — Caching (Phase 4)

| Field | Value |
|---|---|
| Phase | 4 — Caching |
| Roadmap topics | caching, redis, memcached, nosql-databases, http-caching |
| Deliverable | Redis infra + cache-aside `/me` + JWT denylist (`logout`) |
| Milestone | `v0.4.0` |
| Status | ✅ mastery pass |

## Concepts taught
Caching cost model (RAM vs DB vs network); cache-aside; invalidation (TTL,
delete-on-write); TTL vs eviction (LRU/LFU, maxmemory); cache stampede; HTTP
caching (Cache-Control/ETag); Redis (data types, SET EX, persistence); the JWT
denylist for revocation. Full reference: `fundamentals/caching/`, `infra/redis/`,
`security/jwt/`.

## Exercises (recap)
| Q | Topic | Verdict |
|---|---|---|
| R1 | JWT verified without DB (signature + self-contained claims) | ✅ |
| R2 | Identical 401 defends against **user enumeration** | ❌→reviewed |
| R3 | Partial index won on **selectivity** (hot subset) → bridge to caching | ❌→reviewed |

## Built (verified end-to-end)
- `RedisFactory` + `Cache`/`RedisCache` (Lettuce) + `redis()` pipeline stage
- `redis:7-alpine` in docker-compose (AOF, healthcheck)
- Cache-aside on `GET /v1/auth/me` (`cache:user:{id}`, TTL 300s)
- **JWT denylist**: `jti` claim + `POST /v1/auth/logout` + `EXISTS` check in
  validate → **closes Lesson 4 E5** (revoked token → 401)
- 1 new integration test (logout revokes); curl-verified cache hit + denylist

## Gaps to revisit
- HTTP caching headers (Cache-Control/ETag) — documented, not yet applied to a
  response; revisit when public read endpoints exist.

## References
- [lesson_04_auth.md](lesson_04_auth.md) · `[[lesson_04_auth]]`
- [caching_orchestrator.md](../../fundamentals/caching/caching_orchestrator.md) · `[[caching_orchestrator]]`
- [revocation_and_denylist.md](../../security/jwt/revocation_and_denylist.md) · `[[revocation_and_denylist]]`
