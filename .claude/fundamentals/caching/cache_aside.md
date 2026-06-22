---
name: cache_aside
description: Cache-aside (lazy-loading) pattern and its write strategies.
agent:
  role: caching-specialist
  tier: standard
  weight: soft
  triggers:
    - implementing a read cache
    - choosing read-through vs write-through
metadata:
  type: reference
---

# Cache-Aside

The default caching pattern: the application checks the cache, and on a miss
loads from the source and populates the cache.

```
read(key):
  v = cache.get(key)
  if v != null: return v            # hit
  v = db.load(key)                  # miss
  cache.set(key, v, ttl)            # populate
  return v
```

## Write strategies
| Strategy | On write | Trade-off |
|---|---|---|
| **Write-through** | write DB + cache together | cache always fresh; slower writes |
| **Write-around** | write DB only; cache fills on next read | simple; first read after write misses |
| **Write-behind** | write cache, flush to DB async | fast writes; risk of loss before flush |

Cache-aside pairs most often with **write-around + invalidation** (delete the
key on write) — see [invalidation.md](invalidation.md) · `[[invalidation]]`.

## In Nexus (as a Decorator)
The cache wraps a repository without changing it — a textbook Decorator:
```kotlin
class CachingDocumentRepository(
    private val inner: DocumentRepository,
    private val cache: Cache,
) : DocumentRepository by inner {
    override fun findById(id: UUID): Document? =
        cache.get("doc:$id") ?: inner.findById(id)?.also { cache.set("doc:$id", it, ttl = 300) }
}
```
The route is unchanged; caching is a layer (see
[decorator.md](../../designpatterns/kotlin/structural/decorator.md) · `[[decorator]]`).

## References
- [caching_orchestrator.md](caching_orchestrator.md) · `[[caching_orchestrator]]`
- [invalidation.md](invalidation.md) · `[[invalidation]]`
