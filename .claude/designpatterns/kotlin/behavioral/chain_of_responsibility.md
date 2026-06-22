---
name: chain_of_responsibility
description: Chain of Responsibility in idiomatic Kotlin, as applied in nexus-api ModulePipeline.
agent:
  role: pattern-advisor
  tier: standard
  weight: medium
  triggers:
    - building a handler chain, pipeline, or middleware
    - ordering request-processing stages
metadata:
  type: pattern
---

# Chain of Responsibility

## Intent
Give multiple objects a chance to handle a request by chaining them; each
handler either processes the request, passes it on, or aborts the chain.

## Structure
```
Client → Handler₁ → Handler₂ → Handler₃ → (end / null)
              ↓           ↓           ↓
           handle      pass on      handle
```

## Kotlin example
```kotlin
interface Handler<T> { var next: Handler<T>?; fun handle(req: T) }

abstract class BaseHandler<T> : Handler<T> {
    override var next: Handler<T>? = null
    protected fun proceed(req: T) { next?.handle(req) }
}

class AuthHandler : BaseHandler<Request>() {
    override fun handle(req: Request) {
        if (!req.hasToken()) { reject(req, 401); return }   // abort
        proceed(req)                                        // delegate
    }
}
```

## When to use
- A request may be handled by one of several objects, decided at runtime.
- You want ordered, independently-addable processing stages.

## Kotlin idiom
A `List<(T) -> Unit>` or `ArrayDeque` of handler lambdas is often simpler than a
linked-handler hierarchy. Validate ordering at registration time.

## Applied in Nexus — ModulePipeline
`nexus-api` registers plugin stages as an ordered chain
(`logging → serialization → statusPages → database → routing`). `requireStage()`
fails fast if a successor is registered before its prerequisite. The whole chain
is applied by a terminal `build()` — a Builder producing a Chain of Responsibility.

```kotlin
@NexusDsl
class ModulePipeline(private val app: Application) {
    private val chain = ArrayDeque<Application.() -> Unit>()
    fun serialization() = register(SERIALIZATION) { configureSerialization() }
    private fun register(stage: Stage, h: Application.() -> Unit) { /* order check */ chain += h }
    internal fun build() = chain.forEach { app.apply(it) }
}
```

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [dsl.md](../../../services/nexus-api/idioms/dsl.md) · `[[dsl]]`
- [builder.md](../creational/builder.md) · `[[builder]]`
