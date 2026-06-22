---
name: invalidation
description: Cache invalidation strategies — keeping cache consistent with the source.
agent:
  role: caching-specialist
  tier: standard
  weight: medium
  triggers:
    - keeping a cache consistent with the database
    - deciding how stale data is allowed to be
metadata:
  type: reference
---

# Invalidation

> "There are only two hard things in CS: cache invalidation and naming things."

A cache is a *copy*. When the source changes, the copy is stale. Invalidation is
how you bound that staleness.

## Strategies
| Strategy | How | Use when |
|---|---|---|
| **TTL expiry** | entry auto-expires after N seconds | some staleness is fine; simplest |
| **Write-through** | update cache on every write | strong freshness needed |
| **Delete-on-write** | drop the key on write; next read repopulates | common with cache-aside |
| **Event-based** | a change event invalidates keys | distributed / cross-service |

## Delete vs update on write
Prefer **delete** over update: deleting is idempotent and avoids racing a
concurrent read that repopulates with a stale value. The next read reloads fresh.

## Key design
Namespaced, deterministic keys make targeted invalidation possible:
```
doc:{id}            # single document
user:{id}:docs      # a user's document list → delete on add/remove
```
Avoid keys you can't recompute to invalidate — you'll be stuck flushing everything.

## The honest trade-off
Every cache accepts *some* staleness. Decide the tolerance per data type:
- session/denylist → must be correct → short or event-driven
- document body → seconds of staleness fine → TTL

## In Nexus
Document reads use TTL + delete-on-write. The JWT denylist (security) must be
correct, so it uses Redis TTL matched to the token's remaining lifetime — see
[revocation_and_denylist.md](../../security/jwt/revocation_and_denylist.md) · `[[revocation_and_denylist]]`.

## References
- [caching_orchestrator.md](caching_orchestrator.md) · `[[caching_orchestrator]]`
- [ttl_and_eviction.md](ttl_and_eviction.md) · `[[ttl_and_eviction]]`
