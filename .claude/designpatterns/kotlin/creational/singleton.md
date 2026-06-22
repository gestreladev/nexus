---
name: singleton
description: Singleton pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - ensuring a single shared instance
    - sharing a connection pool or registry
metadata:
  type: pattern
---

# Singleton

## Intent
Ensure a class has exactly one instance and provide a global access point.

## Structure
```
Singleton.instance ──► the one and only instance
```

## Kotlin example
```kotlin
// `object` IS a thread-safe, lazily-initialized singleton in Kotlin
object DatabaseFactory {
    private val log = LoggerFactory.getLogger(javaClass)
    fun init(config: DatabaseConfig) { /* one pool per process */ }
}
```

## When to use
- Exactly one instance must coordinate access to a shared resource
  (connection pool, cache client, configuration registry).

## Kotlin idiom
Use `object` — the compiler guarantees single instantiation and thread safety.
No double-checked locking needed. For a singleton that needs constructor
parameters, prefer dependency injection of a single instance over a global.

## Caution
Singletons are global mutable state — they complicate testing. Prefer injecting
one instance (DI) so tests can substitute a fake. `object` is right for
stateless coordinators like `DatabaseFactory`.

## In Nexus
`DatabaseFactory` is an `object` — one HikariCP pool per service process.

## References
- [creational_orchestrator.md](creational_orchestrator.md) · `[[creational_orchestrator]]`
