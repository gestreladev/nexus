---
name: data_classes
description: Kotlin data classes — value holders, copy(), destructuring.
agent:
  role: language-expert
  tier: nano
  weight: soft
  triggers:
    - modeling an immutable value type
    - copying with modifications
metadata:
  type: reference
---

# Data Classes

A `data class` is a value holder. The compiler generates `equals()`,
`hashCode()`, `toString()`, `componentN()`, and `copy()` from the primary
constructor properties.

```kotlin
data class User(
    val id: UUID,
    val email: String,
    val displayName: String,
)

val u = User(id, "a@nexus.dev", "A")
val renamed = u.copy(displayName = "B")   // immutable update — Prototype for free
val (id, email) = u                        // destructuring via componentN()
```

## Idioms
- **Prefer `val`** — immutable data is safer across coroutines/threads.
- **`copy()`** is the idiomatic "change one field" — see the Prototype pattern.
- **Equality is structural** — two data classes with equal properties are `==`.
  Great for test assertions and set/map keys.
- Keep them as *data*, not behavior: business logic belongs in services/domain
  functions, not on the data class.

## Caution
`copy()` is a **shallow** copy — nested mutable structures are shared. Use
immutable nested types (`List`, other data classes) to stay safe.

## In Nexus
`User`, `Document`, and all request/response models are data classes; equality
makes route tests assert on whole objects, and `copy()` powers config tweaks.

## References
- [kotlin_language_orchestrator.md](kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]`
- [prototype.md](../../designpatterns/kotlin/creational/prototype.md) · `[[prototype]]`
