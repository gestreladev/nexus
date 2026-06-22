---
name: bridge
description: Bridge pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - decoupling an abstraction from its implementation
    - varying two dimensions independently
metadata:
  type: pattern
---

# Bridge

## Intent
Decouple an abstraction from its implementation so the two can vary
independently.

## Structure
```
Abstraction ──has-a──► Implementor (interface)
   │                        ▲
RefinedAbstraction    ConcreteImplementorA / B
```

## Kotlin example
```kotlin
// Implementor — how a notification is delivered
interface Channel { fun send(to: String, body: String) }
class EmailChannel : Channel { override fun send(to: String, body: String) {} }
class SmsChannel   : Channel { override fun send(to: String, body: String) {} }

// Abstraction — what kind of notification, holds a Channel
abstract class Notification(protected val channel: Channel) {
    abstract fun notify(user: String)
}
class AlertNotification(channel: Channel) : Notification(channel) {
    override fun notify(user: String) = channel.send(user, "ALERT")
}
```

## When to use
- Two dimensions vary independently (notification type × delivery channel).
- You want to avoid a combinatorial class explosion.

## Kotlin idiom
Constructor-inject the implementor; or pass behavior as a function type
(`(String, String) -> Unit`) for lightweight bridges.

## In Nexus
Notifications (alert/digest) over channels (email/SMS/webhook) vary on two
independent axes — a textbook Bridge.

## References
- [structural_orchestrator.md](structural_orchestrator.md) · `[[structural_orchestrator]]`
- [strategy.md](../behavioral/strategy.md) · `[[strategy]]`
