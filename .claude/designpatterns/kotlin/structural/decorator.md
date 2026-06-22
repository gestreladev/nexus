---
name: decorator
description: Decorator pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - adding behavior to an object without subclassing
    - layering cross-cutting concerns
metadata:
  type: pattern
---

# Decorator

## Intent
Attach additional responsibilities to an object dynamically, as a flexible
alternative to subclassing.

## Structure
```
Component (interface)
   ├── ConcreteComponent
   └── Decorator (wraps Component) ──► ConcreteDecorator (adds behavior)
```

## Kotlin example
```kotlin
interface Repository { fun find(id: String): String? }

class DbRepository : Repository {
    override fun find(id: String): String? = /* hit DB */ null
}

// Decorator adds caching without touching DbRepository
class CachingRepository(private val inner: Repository) : Repository {
    private val cache = mutableMapOf<String, String?>()
    override fun find(id: String) = cache.getOrPut(id) { inner.find(id) }
}

val repo: Repository = CachingRepository(DbRepository())
```

## When to use
- Layer optional concerns (caching, logging, retry) without subclass explosion.
- Compose behaviors at runtime.

## Kotlin idiom
Interface **delegation** (`by`) removes boilerplate — forward everything, override
only what you decorate:
```kotlin
class LoggingRepo(inner: Repository) : Repository by inner {
    override fun find(id: String) = inner.find(id).also { log.info("find $id") }
}
```

## In Nexus
Cross-cutting layers (caching in v0.4.0, retry on external clients) wrap domain
ports as decorators rather than baking concerns into the base implementation.

## References
- [structural_orchestrator.md](structural_orchestrator.md) · `[[structural_orchestrator]]`
- [proxy.md](proxy.md) · `[[proxy]]`
