---
name: builder
description: Builder pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - constructing a complex object step by step
    - building a type-safe DSL
metadata:
  type: pattern
---

# Builder

## Intent
Separate the construction of a complex object from its representation, so the
same process can build different representations.

## Structure
```
Builder.partA() → Builder.partB() → Builder.build() → Product
```

## Kotlin example
```kotlin
class HttpRequest private constructor(
    val url: String, val method: String, val headers: Map<String, String>,
) {
    class Builder(private val url: String) {
        private var method = "GET"
        private val headers = mutableMapOf<String, String>()
        fun method(m: String) = apply { method = m }
        fun header(k: String, v: String) = apply { headers[k] = v }
        fun build() = HttpRequest(url, method, headers)
    }
}

val req = HttpRequest.Builder("https://api.nexus.dev")
    .method("POST").header("Authorization", "Bearer …").build()
```

## When to use
- Many optional parameters (telescoping-constructor smell).
- Construction must be validated as a whole before producing the object.

## Kotlin idioms
- **Default + named arguments** often replace Builder entirely:
  `HttpRequest(url, method = "POST")`.
- For nested/declarative construction, use a **DSL builder** with
  `apply`/lambda-with-receiver (see idioms/dsl).
- `apply { }` returns the receiver — ideal for fluent setters.

## In Nexus
`ModulePipeline` is a deferred-execution builder: stages register, then
`build()` applies them in order. See the DSL idiom and Chain of Responsibility.

## References
- [creational_orchestrator.md](creational_orchestrator.md) · `[[creational_orchestrator]]`
- [dsl.md](../../../services/nexus-api/idioms/dsl.md) · `[[dsl]]`
