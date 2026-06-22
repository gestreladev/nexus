---
name: dsl
description: Kotlin DSL idioms — extension functions, lambdas with receivers, @DslMarker.
agent:
  role: ktor-specialist
  tier: standard
  weight: medium
  triggers:
    - writing or reviewing a Kotlin DSL
    - using @DslMarker or builder patterns
metadata:
  type: reference
---

# Kotlin DSL

## What is a DSL

A Domain-Specific Language reads like a description of intent. In Kotlin, DSLs
are built from three features: extension functions, lambdas with receivers, and
`@DslMarker`.

## Extension functions as vocabulary

```kotlin
fun Route.healthRoutes() {        // extends Ktor's Route
    get("/health") { ... }
}
```

Callers write `healthRoutes()` inside any `Route` block — reads as configuration.

## Lambdas with receivers

A lambda with receiver `T.() -> Unit` is a block where `this` is a `T`.

```kotlin
fun html(block: HtmlBuilder.() -> Unit): HtmlBuilder {
    val builder = HtmlBuilder()
    builder.block()   // 'this' inside block is the HtmlBuilder
    return builder
}

html {
    head { title("Nexus") }
    body { p("Hello") }
}
```

This is what makes `nexusModule { logging(); serialization() }` possible:
inside the lambda, `this` is `ModulePipeline`.

## `@DslMarker`

Without it, nested lambdas can call outer receivers by accident:

```kotlin
@DslMarker
annotation class HtmlDsl

@HtmlDsl class HtmlBuilder { ... }
@HtmlDsl class BodyBuilder { ... }

html {
    body {
        head { }   // compile error — HtmlBuilder not accessible here
    }
}
```

`@DslMarker` makes implicit outer receivers invisible inside nested blocks.

## Builder pattern + DSL

```kotlin
@DslMarker
annotation class NexusDsl

@NexusDsl
class ModulePipeline(private val app: Application) {
    private val chain = ArrayDeque<Application.() -> Unit>()
    fun serialization() { chain += { configureSerialization() } }
    fun routing()       { chain += { configureRouting() } }
    internal fun build() = chain.forEach { app.apply(it) }
}

fun Application.nexusModule(block: ModulePipeline.() -> Unit) {
    ModulePipeline(this).also(block).build()
}
```

**Execution is deferred**: `build()` runs only after the whole `block` is
evaluated, enabling validation before any side effects.

## Rules of thumb

| Rule | Reason |
|---|---|
| Annotate every receiver with your `@DslMarker` annotation | Prevents scope leakage |
| Prefer `also(block)` when you need the instance after | `also` returns the receiver |
| Keep vocabulary short (nouns/verbs) | DSLs should read, not explain |
| Defer execution to a terminal `build()` | Validate before side effects |

## References
- [idioms_orchestrator.md](idioms_orchestrator.md) · `[[idioms_orchestrator]]`
- [chain_of_responsibility.md](../../../designpatterns/kotlin/behavioral/chain_of_responsibility.md) · `[[chain_of_responsibility]]`
