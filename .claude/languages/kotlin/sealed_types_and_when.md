---
name: sealed_types_and_when
description: Sealed interfaces/classes, data object, and exhaustive when.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - modeling a closed set of types
    - using when over a sealed hierarchy
metadata:
  type: reference
---

# Sealed Types & `when`

A `sealed` interface/class declares a **closed** set of subtypes known at compile
time. `when` over a sealed type is **exhaustive** — the compiler errors if you
miss a case, so adding a new subtype surfaces every place that must handle it.

```kotlin
sealed interface DocStatus
data object Pending : DocStatus
data object Processing : DocStatus
data object Ready : DocStatus
data class Failed(val reason: String) : DocStatus

fun label(s: DocStatus): String = when (s) {   // no else needed — exhaustive
    Pending    -> "queued"
    Processing -> "working"
    Ready      -> "done"
    is Failed  -> "failed: ${s.reason}"
}
```

## `data object` (Kotlin 1.9+/2.x)
For stateless singletons in a sealed hierarchy, `data object` gives a sensible
`toString()`/`equals()` — cleaner than a bare `object`.

## Why it's powerful
- **No `else` trap:** with `else`, the compiler can't warn you about new cases.
  Omit `else` on sealed `when` so additions stay safe.
- Models domain states, results, and AST nodes precisely (see State, Visitor,
  Interpreter patterns).

## In Nexus
The `documents.status` lifecycle and any `Result`-style return are natural sealed
hierarchies — exhaustive `when` keeps transitions honest.

## References
- [kotlin_language_orchestrator.md](kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]`
- [state.md](../../designpatterns/kotlin/behavioral/state.md) · `[[state]]`
