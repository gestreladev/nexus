---
name: mediator
description: Mediator pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - centralizing complex many-to-many communication
    - reducing direct coupling between components
metadata:
  type: pattern
---

# Mediator

## Intent
Define an object that encapsulates how a set of objects interact, promoting
loose coupling by keeping them from referring to each other directly.

## Structure
```
ColleagueA ─┐
ColleagueB ─┼─► Mediator ─► routes between colleagues
ColleagueC ─┘
```

## Kotlin example
```kotlin
interface Mediator { fun publish(event: String, from: String) }

class EventHub : Mediator {
    private val subscribers = mutableMapOf<String, (String) -> Unit>()
    fun register(name: String, onEvent: (String) -> Unit) { subscribers[name] = onEvent }
    override fun publish(event: String, from: String) =
        subscribers.filterKeys { it != from }.values.forEach { it(event) }
}
```

## When to use
- Many components communicate in a tangled many-to-many web.
- You want interaction logic in one place instead of spread across colleagues.

## Mediator vs Observer
Observer is one-to-many broadcast from a subject. Mediator coordinates
many-to-many and can encode routing/business rules between participants.

## In Nexus
An in-process event hub coordinates ingestion-stage components without each
holding references to the others (precursor to the Kafka bus in v0.7.0).

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [observer.md](observer.md) · `[[observer]]`
