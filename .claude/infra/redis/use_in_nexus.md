---
name: use_in_nexus
description: How Nexus uses Redis — cache layer, JWT denylist, key conventions.
agent:
  role: infra-specialist
  tier: standard
  weight: soft
  triggers:
    - deciding what to store in Redis
    - naming Redis keys
metadata:
  type: reference
---

# Redis in Nexus

One Redis instance, three jobs (rolled out across phases):

| Job | Phase | Pattern |
|---|---|---|
| **Cache-aside** for hot reads | 4 (now) | `String` + TTL; delete-on-write |
| **JWT denylist** for revocation | 4 (closes E5) | key per revoked token id; TTL = token's remaining life |
| **Sessions / rate-limit** | later | hashes / counters with TTL |

## Key conventions
Namespaced and deterministic so invalidation is targeted:
```
cache:doc:{id}            # cached document
cache:user:{id}:docs      # a user's document list (delete on add/remove)
denylist:jwt:{jti}        # presence = revoked; EXISTS check on each request
```

## Cache layer (Decorator)
A `CachingDocumentRepository` wraps the real repository, adding cache-aside
without touching routes — see
[cache_aside.md](../../fundamentals/caching/cache_aside.md) · `[[cache_aside]]`
and [decorator.md](../../designpatterns/kotlin/structural/decorator.md) · `[[decorator]]`.

## JWT denylist (the E5 payoff)
On logout/revoke, store `denylist:jwt:{jti}` with `EX` = seconds until the token
would expire. The JWT `validate` step does `EXISTS denylist:jwt:{jti}` and rejects
if present — giving real revocation despite stateless tokens. Details:
[revocation_and_denylist.md](../../security/jwt/revocation_and_denylist.md) · `[[revocation_and_denylist]]`.

## Failure mode
If Redis is down: the cache should **fail open** (fall back to the DB), but the
denylist must **fail closed** for sensitive revocation — or rely on short token
TTLs. A deliberate trade-off recorded when built.

## References
- [redis_orchestrator.md](redis_orchestrator.md) · `[[redis_orchestrator]]`
- [compose_and_client.md](compose_and_client.md) · `[[compose_and_client]]`
