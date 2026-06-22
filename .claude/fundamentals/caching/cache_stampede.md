---
name: cache_stampede
description: Cache stampede (thundering herd) and mitigations.
agent:
  role: caching-specialist
  tier: standard
  weight: soft
  triggers:
    - a hot key expiring under load
    - protecting the source from a surge of misses
metadata:
  type: reference
---

# Cache Stampede

When a hot key expires, every concurrent request misses **at once** and they all
hit the source together — a "thundering herd" that can overload the DB precisely
when traffic is highest.

```
t=0   doc:hot expires
t=0+  1000 requests miss simultaneously
      → 1000 identical DB queries fire        ← the stampede
```

## Mitigations
| Technique | Idea |
|---|---|
| **Locking / single-flight** | first miss takes a lock and loads; others wait for the result |
| **Early/probabilistic expiry** | refresh slightly *before* TTL so it never hard-expires under load |
| **Stale-while-revalidate** | serve the stale value while one worker refreshes |
| **Jittered TTLs** | randomize TTLs so many keys don't expire on the same tick |

## Single-flight sketch
```kotlin
suspend fun get(key: String): V =
    cache.get(key) ?: mutexFor(key).withLock {
        cache.get(key) ?: source.load(key).also { cache.set(key, it, ttl) }  // double-check
    }
```
The double-check inside the lock ensures only the first waiter loads; the rest
read the freshly-populated value.

## In Nexus
Hot document reads use a short jittered TTL; if a single key proves very hot
under load testing (Phase 12), add single-flight around its loader.

## References
- [caching_orchestrator.md](caching_orchestrator.md) · `[[caching_orchestrator]]`
- [ttl_and_eviction.md](ttl_and_eviction.md) · `[[ttl_and_eviction]]`
