---
name: proxy
description: Proxy pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - controlling access to an object
    - adding lazy-loading, rate-limiting, or auth in front of a service
metadata:
  type: pattern
---

# Proxy

## Intent
Provide a surrogate or placeholder for another object to control access to it.

## Structure
```
Client ──► Subject (interface)
              ▲
           Proxy ──► RealSubject (controlled access)
```

## Kotlin example
```kotlin
interface LlmClient { fun complete(prompt: String): String }

class RealLlmClient : LlmClient {
    override fun complete(prompt: String): String = /* network call */ ""
}

// Rate-limiting proxy guards the real client
class RateLimitedLlmClient(
    private val real: LlmClient,
    private val limiter: RateLimiter,
) : LlmClient {
    override fun complete(prompt: String): String {
        require(limiter.tryAcquire()) { "Rate limit exceeded" }
        return real.complete(prompt)
    }
}
```

## When to use
- Lazy initialization (virtual proxy), access control (protection proxy),
  remote access, rate limiting, or caching in front of a real object.

## Proxy vs Decorator
Same shape (wrap + same interface). **Intent differs:** Decorator *adds*
behavior; Proxy *controls access* (when/whether the real call happens).

## In Nexus
The LLM client (v0.11.0) sits behind a proxy enforcing rate limits and
budget caps before any request reaches the provider.

## References
- [structural_orchestrator.md](structural_orchestrator.md) · `[[structural_orchestrator]]`
- [decorator.md](decorator.md) · `[[decorator]]`
