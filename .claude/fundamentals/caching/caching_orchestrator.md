---
name: caching_orchestrator
description: Routing for caching fundamentals — patterns, invalidation, TTL, HTTP caching.
agent:
  role: caching-specialist
  tier: standard
  weight: medium
  triggers:
    - adding or reviewing a cache
    - choosing a caching pattern or invalidation strategy
metadata:
  type: orchestrator
---

# Caching — Fundamentals

Why and how to cache. A cache trades memory + staleness risk for latency: serve
the hot, small subset of data from fast storage instead of recomputing/refetching.
Concepts are vendor-neutral; the concrete store is
[redis_orchestrator.md](../../infra/redis/redis_orchestrator.md) · `[[redis_orchestrator]]`.
Taught in Lesson 5 (Phase 4). Model selection:
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Cost model (why it works)
| Tier | Rough latency |
|---|---|
| In-process memory | ~10–100 ns |
| Redis (network, RAM) | ~0.1–1 ms |
| PostgreSQL query | ~1–10 ms+ |
| External API / LLM | ~100 ms–seconds |

A cache moves a repeated read down this ladder. Worth it when reads ≫ writes and
the data tolerates some staleness.

## Routing Table
| Topic | Scope | Document |
|---|---|---|
| Cache-aside | Lazy-load pattern (read-through) | [cache_aside.md](cache_aside.md) · `[[cache_aside]]` |
| Invalidation | Keeping cache and source consistent | [invalidation.md](invalidation.md) · `[[invalidation]]` |
| TTL & eviction | Expiry, maxmemory, LRU/LFU | [ttl_and_eviction.md](ttl_and_eviction.md) · `[[ttl_and_eviction]]` |
| Cache stampede | Thundering herd + mitigations | [cache_stampede.md](cache_stampede.md) · `[[cache_stampede]]` |
| HTTP caching | Cache-Control, ETag, conditional GET | [http_caching.md](http_caching.md) · `[[http_caching]]` |

## Cache store choice — Redis vs Memcached
- **Memcached** — pure, multi-threaded in-memory KV cache; dead simple, no
  persistence, no data types. Great when you want *only* an LRU cache.
- **Redis** — richer (data types, TTL, persistence, pub/sub), single logical
  store reusable for cache **and** denylist/sessions. Nexus picks **Redis** to
  avoid running two systems — see
  [redis_orchestrator.md](../../infra/redis/redis_orchestrator.md) · `[[redis_orchestrator]]`.

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [decorator.md](../../designpatterns/kotlin/structural/decorator.md) · `[[decorator]]` — how the cache layer wraps a repository
- [nosql.md](../databases/nosql.md) · `[[nosql]]`
