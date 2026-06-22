---
name: delegation
description: Kotlin delegation — interface delegation and delegated properties via by.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - forwarding an interface to a wrapped instance
    - using delegated properties (lazy, observable)
metadata:
  type: reference
---

# Delegation (`by`)

Kotlin makes the Delegation pattern first-class with the `by` keyword.

## Interface delegation
Forward all of an interface to a wrapped instance, override only what you change
— removes boilerplate for Decorator/Proxy.

```kotlin
interface Repository { fun find(id: String): String? }

class LoggingRepo(private val inner: Repository) : Repository by inner {
    override fun find(id: String) =
        inner.find(id).also { log.info("find $id") }   // decorate one method
}
```

`by inner` generates the pass-through methods; you override `find` only.

## Delegated properties
A property can delegate its get/set to a helper:

```kotlin
val config: Config by lazy { loadConfig() }       // computed once, on first use
var name: String by Delegates.observable("") { _, old, new -> log(old, new) }
val port: Int by environment.config               // map/config-backed
```

Common delegates: `lazy` (thread-safe one-time init), `Delegates.observable`
(change callback), map-backed, and custom `ReadOnlyProperty`/`ReadWriteProperty`.

## In Nexus
Interface delegation is the clean way to build the caching/logging **decorators**
(Phase 4) over a `Repository`; `by lazy` fits expensive one-time initializers.

## References
- [kotlin_language_orchestrator.md](kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]`
- [decorator.md](../../designpatterns/kotlin/structural/decorator.md) · `[[decorator]]`
- [proxy.md](../../designpatterns/kotlin/structural/proxy.md) · `[[proxy]]`
