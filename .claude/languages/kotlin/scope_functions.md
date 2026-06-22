---
name: scope_functions
description: Kotlin scope functions — let, run, with, apply, also.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - choosing between let/run/with/apply/also
    - configuring an object inline
metadata:
  type: reference
---

# Scope Functions

Five functions run a block in the context of an object. They differ on two axes:
**how the object is referenced** (`this` vs `it`) and **what they return**
(the object vs the block result).

| Function | Context ref | Returns | Typical use |
|---|---|---|---|
| `let` | `it` | block result | null-guard + transform |
| `run` | `this` | block result | compute from an object's members |
| `with` | `this` | block result | group calls on one object |
| `apply` | `this` | the object | configure then return it |
| `also` | `it` | the object | side-effect (log) in a chain |

```kotlin
// apply — configure and return (builder-ish)
val cfg = HikariConfig().apply {
    jdbcUrl = url; maximumPoolSize = 10
}

// let — run only if non-null, transform
val len = name?.let { it.trim().length } ?: 0

// also — side effect without breaking the chain
repo.find(id).also { log.info("found $it") }
```

## Choosing (rule of thumb)
- Need the **object back** → `apply` (config) or `also` (side effect).
- Need the **block result** → `run`/`with` (use `this`) or `let` (use `it`).
- **Null-guarding** → `let` with `?.`.

## In Nexus
`apply` configures `HikariConfig`; `ModulePipeline` uses `also(block)` to run the
DSL block and return the pipeline before `build()` (see DSL idiom).

## References
- [kotlin_language_orchestrator.md](kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]`
- [dsl.md](../../services/nexus-api/idioms/dsl.md) · `[[dsl]]`
