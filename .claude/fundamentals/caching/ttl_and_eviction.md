---
name: ttl_and_eviction
description: TTL expiry and memory eviction policies for caches.
agent:
  role: caching-specialist
  tier: standard
  weight: soft
  triggers:
    - setting a TTL
    - configuring maxmemory / eviction
metadata:
  type: reference
---

# TTL & Eviction

Two different mechanisms remove cache entries — don't confuse them.

## TTL (time-to-live) — per key
You set an expiry; the store auto-deletes the key when it lapses. This is the
primary staleness control.
```
SET doc:123 "..." EX 300      # expire in 300s
TTL doc:123                   # seconds remaining
```
Choose TTL by tolerance: short for volatile data, long for stable data. A short
TTL is also a cheap safety net against stale bugs.

## Eviction — under memory pressure
When Redis hits `maxmemory`, the `maxmemory-policy` decides what to drop:
| Policy | Evicts |
|---|---|
| `noeviction` | nothing — writes error (default; risky for a cache) |
| `allkeys-lru` | least-recently-used across all keys |
| `allkeys-lfu` | least-frequently-used |
| `volatile-lru` | LRU among keys that have a TTL |
| `volatile-ttl` | keys with the nearest expiry first |

For a **pure cache**, `allkeys-lru` (or `lfu`) is typical — let cold keys fall
out automatically. If the same Redis also holds must-not-lose data (a denylist),
prefer `volatile-*` and give that data no TTL, or isolate it.

## TTL vs eviction
- **TTL** = correctness/freshness (you chose it).
- **Eviction** = capacity safety (the store chose it under pressure).

## In Nexus
Document cache entries carry a TTL (e.g. 300s). Denylist entries use a TTL equal
to the token's remaining life, so they self-clean exactly when the token would
have expired anyway.

## References
- [caching_orchestrator.md](caching_orchestrator.md) · `[[caching_orchestrator]]`
- [redis_orchestrator.md](../../infra/redis/redis_orchestrator.md) · `[[redis_orchestrator]]`
