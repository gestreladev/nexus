---
name: flyweight
description: Flyweight pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - sharing immutable state to save memory
    - interning many similar objects
metadata:
  type: pattern
---

# Flyweight

## Intent
Use sharing to support large numbers of fine-grained objects efficiently by
separating intrinsic (shared) from extrinsic (context) state.

## Structure
```
FlyweightFactory.get(key) ──► cached shared Flyweight
Client supplies extrinsic state per use
```

## Kotlin example
```kotlin
data class Token(val text: String)            // intrinsic, shared

object TokenPool {                             // flyweight factory
    private val pool = mutableMapOf<String, Token>()
    fun intern(text: String): Token = pool.getOrPut(text) { Token(text) }
}

// 1M occurrences of "the" share one Token instance
val a = TokenPool.intern("the")
val b = TokenPool.intern("the")
check(a === b)
```

## When to use
- Huge numbers of objects with heavily-duplicated immutable state.
- Memory pressure is real and the shared state is truly immutable.

## Kotlin idiom
A `getOrPut`-backed cache in an `object`. Shared values must be immutable
(`data class` with `val`s). Don't over-apply — premature interning adds
contention for little gain.

## In Nexus
Embedding/token vocabularies and repeated metadata keys can be interned to cut
memory when processing large corpora in `nexus-ingest`.

## References
- [structural_orchestrator.md](structural_orchestrator.md) · `[[structural_orchestrator]]`
