# Kotlin DSL

## What is a DSL

A Domain-Specific Language is an API designed to read like a description of
intent rather than a sequence of instructions. In Kotlin, DSLs are built from
three features: extension functions, lambdas with receivers, and `@DslMarker`.

## Extension functions as vocabulary

Extension functions add methods to types you don't own. They are the words of
your DSL:

```kotlin
fun Route.healthRoutes() {        // extends Ktor's Route
    get("/health") { ... }
}
```

Callers write `healthRoutes()` inside any `Route` block — it reads as a
configuration declaration, not a function call.

## Lambdas with receivers

A lambda with receiver `T.() -> Unit` is a block where `this` is an instance
of `T`. The caller writes code as if they are inside `T`.

```kotlin
fun html(block: HtmlBuilder.() -> Unit): HtmlBuilder {
    val builder = HtmlBuilder()
    builder.block()   // 'this' inside block is the HtmlBuilder
    return builder
}

// usage — no explicit receiver needed, reads like markup
html {
    head { title("Nexus") }
    body { p("Hello") }
}
```

This is what makes `nexusModule { logging(); serialization() }` possible:
inside the lambda, `this` is `ModulePipeline`.

## `@DslMarker`

Without `@DslMarker`, nested lambdas can call methods from outer receivers:

```kotlin
html {
    body {
        head { }   // 'head' is on HtmlBuilder (outer), not BodyBuilder — compiles but is wrong
    }
}
```

`@DslMarker` prevents this by making implicit outer receivers invisible inside
nested blocks. Only the innermost `@DslMarker`-annotated receiver is in scope.

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

## Builder pattern + DSL

Builders collect configuration, validate it, then execute once. The DSL is the
interface; the builder is the implementation.

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

**Execution is deferred**: `build()` runs only after the entire `block` is
evaluated. This enables validation before any side effects occur.

## Rules of thumb

| Rule | Reason |
|---|---|
| Annotate every receiver class with your `@DslMarker` annotation | Prevents scope leakage in all nested blocks |
| Prefer `also(block)` over `apply(block)` when you need the instance after | `also` returns the receiver; `apply` returns the result of the block |
| Keep DSL vocabulary (method names) as short nouns or verbs | DSLs should read, not explain |
| Defer execution to a terminal `build()` / `execute()` call | Validates the complete configuration before side effects |
| Use `check()` / `require()` inside builders, not inside DSL methods | Separate constraint from vocabulary |
