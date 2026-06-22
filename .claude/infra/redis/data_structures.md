---
name: data_structures
description: Redis data types and key expiration commands.
agent:
  role: infra-specialist
  tier: standard
  weight: soft
  triggers:
    - choosing a Redis data type
    - setting key expiry
metadata:
  type: reference
---

# Redis Data Structures

Redis keys map to typed values — pick the type that fits the access pattern.
(Commands verified via Context7 / redis.io.)

| Type | Use | Key commands |
|---|---|---|
| String | cached value, counter, flag | `SET`, `GET`, `INCR`, `SETEX` |
| Hash | object with fields | `HSET`, `HGET`, `HGETALL` |
| Set | unique membership | `SADD`, `SISMEMBER`, `SREM` |
| Sorted set | ranking, leaderboards, time windows | `ZADD`, `ZRANGE` |
| List | queue / stack | `LPUSH`, `RPOP` |

## Expiry (the cache/denylist workhorse)
```
SET doc:123 "..." EX 300      # value + 300s TTL in one command
SETEX doc:123 300 "..."       # equivalent
EXPIRE doc:123 300            # add/replace TTL on an existing key
TTL doc:123                   # seconds remaining (-1 none, -2 missing)
DEL doc:123                   # remove now (invalidation)
EXISTS denylist:jti           # membership check (denylist)
```
Expired keys are deleted automatically — no sweeper to run.

## In Nexus
- **Cache** → `String` with `EX <ttl>` (cache-aside values).
- **Denylist** → a key per revoked token id (`EXISTS` check), `EX` = token's
  remaining life so it self-cleans.

## References
- [redis_orchestrator.md](redis_orchestrator.md) · `[[redis_orchestrator]]`
- [ttl_and_eviction.md](../../fundamentals/caching/ttl_and_eviction.md) · `[[ttl_and_eviction]]`
