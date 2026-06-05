# Chain of Responsibility

## Intent

Decouple the sender of a request from its receivers by giving multiple objects
a chance to handle it. Handlers are chained; each decides to handle, pass, or
abort.

## When to use

- A request may be handled by more than one object and the handler is not known
  a priori.
- You want to issue a request to one of several objects without specifying the
  receiver explicitly.
- The set of handlers should be configurable or extensible at runtime.

## Structure

```
Client → Handler₁ → Handler₂ → Handler₃ → (end / null)
              ↓           ↓           ↓
           handle      pass on      handle
```

Each handler holds an optional reference to the next handler. Handling means
either processing the request and stopping, or delegating to the successor.

## Agnostic Implementation

```
// --- Abstraction ---

interface Handler<T> {
    var next: Handler<T>?
    fun handle(request: T)
}

// Convenience base — subclasses call proceed() to delegate
abstract class BaseHandler<T> : Handler<T> {
    override var next: Handler<T>? = null

    protected fun proceed(request: T) {
        next?.handle(request) ?: unhandled(request)
    }

    open fun unhandled(request: T) { /* no-op by default */ }
}

// --- Concrete handlers ---

class AuthHandler : BaseHandler<Request>() {
    override fun handle(request: Request) {
        if (!request.hasToken()) {
            reject(request, 401)
            return               // chain stops here
        }
        proceed(request)         // delegate to next
    }
}

class RateLimitHandler : BaseHandler<Request>() {
    override fun handle(request: Request) {
        if (isOverLimit(request.clientId)) {
            reject(request, 429)
            return
        }
        proceed(request)
    }
}

class BusinessHandler : BaseHandler<Request>() {
    override fun handle(request: Request) {
        process(request)         // terminal handler — no proceed()
    }
}

// --- Assembly ---

fun buildChain(): Handler<Request> {
    val auth      = AuthHandler()
    val rateLimit = RateLimitHandler()
    val business  = BusinessHandler()

    auth.next      = rateLimit
    rateLimit.next = business

    return auth   // caller only knows the head
}

// --- Usage ---

val chain = buildChain()
chain.handle(incomingRequest)
```

## Key properties

| Property | Description |
|---|---|
| **Open/Closed** | Add a new handler without touching existing ones |
| **Single Responsibility** | Each handler does one thing |
| **Fail-fast** | Any handler can abort the chain early |
| **Order matters** | Auth before rate-limit before business logic |

## In Nexus

`ModulePipeline` (in `nexus-api/pipeline/ModulePipeline.kt`) applies this
pattern: `logging → serialization → statusPages → routing` is an ordered
chain of Ktor plugin registrations. Each stage is a handler; `requireStage()`
enforces that predecessor handlers are registered before a successor runs.
