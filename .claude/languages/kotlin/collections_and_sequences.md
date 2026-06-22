---
name: collections_and_sequences
description: Kotlin collections vs sequences — eager vs lazy pipelines.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - transforming collections
    - deciding eager vs lazy evaluation
metadata:
  type: reference
---

# Collections & Sequences

## Collection operators (eager)
`map`, `filter`, `groupBy`, `associateBy`, `sumOf`, `partition`, etc. produce a
new collection at **each step**.

```kotlin
val emails = users
    .filter { it.active }
    .map { it.email }          // intermediate List created per step
```

For small collections this is clear and fast. The cost: each operator
materializes a full intermediate list.

## Sequences (lazy)
`asSequence()` makes the pipeline **lazy** — elements flow through all operators
one at a time, nothing materialized until a terminal op (`toList`, `first`,
`sumOf`). Best for large data or early termination.

```kotlin
val firstMatch = docs.asSequence()
    .filter { it.status == Ready }
    .map { it.title }
    .first()                   // stops at the first — later items never processed
```

## Choosing
| Use | When |
|---|---|
| Collection ops | small/medium data, simple chains |
| `asSequence()` | large data, many chained ops, or short-circuit (`first`/`any`) |

Other staples: `associateBy { }` (→ Map by key), `groupBy { }`, `sumOf { }`,
`mapNotNull { }` (map + drop nulls), `flatMap { }`.

## In Nexus
Repository result mapping uses collection ops on small lists; large document/
chunk streams (ingest, Phase 6+) use sequences or Flow to avoid loading
everything into memory (see Iterator pattern).

## References
- [kotlin_language_orchestrator.md](kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]`
- [iterator.md](../../designpatterns/kotlin/behavioral/iterator.md) · `[[iterator]]`
