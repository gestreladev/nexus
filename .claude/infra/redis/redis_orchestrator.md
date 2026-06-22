---
name: redis_orchestrator
description: Routing for Redis — the in-memory backing service for caching, denylist, sessions.
agent:
  role: infra-specialist
  tier: standard
  weight: medium
  triggers:
    - using Redis (cache, denylist, sessions)
    - adding Redis to docker compose or the Kotlin client
metadata:
  type: orchestrator
---

# Redis Orchestrator — Nexus

Redis is an in-memory key-value store used by Nexus as a **backing service** for
three jobs: the cache layer (Phase 4), the JWT denylist (security), and later
sessions/rate-limits. It lives in `infra/` because it's a deployed dependency,
beside [docker_orchestrator.md](../docker/docker_orchestrator.md) · `[[docker_orchestrator]]`.
Content verified via Context7 (redis.io). Model selection:
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Metadata
| Field | Value |
|---|---|
| Image | `redis:7-alpine` (compose service `redis`, port 6379) |
| Kotlin client | Lettuce (thread-safe, sync/async/reactive) |
| Persistence | RDB + AOF (see persistence) |
| Roles in Nexus | cache-aside · JWT denylist · (future) sessions, rate-limit |

---

## Routing Table
| Topic | Scope | Document |
|---|---|---|
| Data structures | strings, hashes, sets, sorted sets, TTL | [data_structures.md](data_structures.md) · `[[data_structures]]` |
| Persistence | RDB snapshots vs AOF | [persistence.md](persistence.md) · `[[persistence]]` |
| Compose & client | docker-compose service + Lettuce setup | [compose_and_client.md](compose_and_client.md) · `[[compose_and_client]]` |
| Use in Nexus | cache layer, JWT denylist, key conventions | [use_in_nexus.md](use_in_nexus.md) · `[[use_in_nexus]]` |

## References
- [infra_orchestrator.md](../infra_orchestrator.md) · `[[infra_orchestrator]]`
- [caching_orchestrator.md](../../fundamentals/caching/caching_orchestrator.md) · `[[caching_orchestrator]]`
- [jwt_orchestrator.md](../../security/jwt/jwt_orchestrator.md) · `[[jwt_orchestrator]]`
