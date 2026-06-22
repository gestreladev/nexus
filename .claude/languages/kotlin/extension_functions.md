---
name: extension_functions
description: Kotlin extension functions — adding behavior without inheritance.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - adding behavior to a type you don't own
    - building readable DSL vocabulary
metadata:
  type: reference
---

# Extension Functions

An extension function adds a method to a type you don't own, without subclassing.
It's resolved **statically** (by the declared type, not runtime polymorphism).

```kotlin
fun String.slugify(): String =
    lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')

"Hello World!".slugify()   // "hello-world"
```

## Receivers and DSLs
With a **lambda receiver** `T.() -> Unit`, extensions become DSL vocabulary —
inside the block, `this` is the receiver:

```kotlin
fun Route.healthRoutes() {     // extends Ktor's Route
    get("/health") { ... }
}
```

This is how Ktor routing and `ModulePipeline` read like configuration.

## Idioms & cautions
- Great for **mapping** (`ResultRow.toUser()`), **vocabulary** (route builders),
  and small utilities — keep the codebase reading like the domain.
- **Private extensions** keep helpers scoped (the repository's `toUser()`).
- Static dispatch: an extension does **not** override a member with the same
  name; the member wins. Don't rely on it for polymorphism.

## In Nexus
`Route.healthRoutes()`, `Application.module()`, and private `ResultRow.toX()`
mappers are all extension functions — the backbone of the readable layering.

## References
- [kotlin_language_orchestrator.md](kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]`
- [dsl.md](../../services/nexus-api/idioms/dsl.md) · `[[dsl]]`
