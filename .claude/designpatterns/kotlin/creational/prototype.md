---
name: prototype
description: Prototype pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - cloning an existing configured object
    - producing variants from a template instance
metadata:
  type: pattern
---

# Prototype

## Intent
Create new objects by cloning an existing instance (the prototype) rather than
constructing from scratch.

## Structure
```
Prototype.clone() ──► new copy (optionally with tweaks)
```

## Kotlin example
```kotlin
data class QueryConfig(
    val index: String,
    val topK: Int = 10,
    val filters: Map<String, String> = emptyMap(),
)

val base = QueryConfig(index = "documents")
// copy() IS the Prototype operation in Kotlin
val narrowed = base.copy(topK = 3, filters = mapOf("status" to "ready"))
```

## When to use
- Object creation is expensive and a configured instance already exists.
- You need many near-identical variants of a template.

## Kotlin idiom
`data class` + `copy()` gives Prototype for free with a **shallow** copy.
For deep copies, copy nested mutable structures explicitly inside a helper.

## In Nexus
Default request/config objects (e.g. a base `QueryConfig`) are cloned with
`copy()` per request instead of rebuilt.

## References
- [creational_orchestrator.md](creational_orchestrator.md) · `[[creational_orchestrator]]`
