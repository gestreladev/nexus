---
name: observer
description: Observer pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: medium
  triggers:
    - notifying dependents of state changes
    - building event/subscription flows
metadata:
  type: pattern
---

# Observer

## Intent
Define a one-to-many dependency so that when one object changes state, all its
dependents are notified automatically.

## Structure
```
Subject.subscribe(observer)
Subject.notify() ──► observer.update(state) for each observer
```

## Kotlin example
```kotlin
class Subject<T>(initial: T) {
    private val observers = mutableListOf<(T) -> Unit>()
    var value: T = initial
        set(new) { field = new; observers.forEach { it(new) } }   // notify on change
    fun subscribe(observer: (T) -> Unit) { observers += observer }
}

val status = Subject("pending")
status.subscribe { println("now: $it") }
status.value = "processing"   // prints "now: processing"
```

## When to use
- State changes must fan out to multiple interested parties.
- Decouple the source of a change from its reactions.

## Kotlin idioms
- `Delegates.observable()` for property-change callbacks.
- **Kotlin Flow / coroutines** for asynchronous, backpressure-aware streams —
  the modern, preferred form for real event pipelines.

## In Nexus
Document status transitions (`pending → processing → ready`) publish events that
observers (notifications, metrics) react to. At scale this becomes Kafka (v0.7.0).

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [mediator.md](mediator.md) · `[[mediator]]`
