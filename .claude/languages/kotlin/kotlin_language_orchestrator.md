---
name: kotlin_language_orchestrator
description: Routing for Kotlin language features and idioms (the language itself).
agent:
  role: language-expert
  tier: standard
  weight: medium
  triggers:
    - using a Kotlin language feature
    - writing idiomatic Kotlin in nexus-api
metadata:
  type: orchestrator
---

# Kotlin — Language Orchestrator

Reference for the **Kotlin language** as used in `nexus-api` (Kotlin **2.1.21**,
K2 compiler). For GoF patterns in Kotlin see
[kotlin_orchestrator.md](../../designpatterns/kotlin/kotlin_orchestrator.md) · `[[kotlin_orchestrator]]`;
for project DSL/Exposed idioms see
[idioms_orchestrator.md](../../services/nexus-api/idioms/idioms_orchestrator.md) · `[[idioms_orchestrator]]`.

Model selection governed by
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Metadata

| Field | Value |
|---|---|
| Version | Kotlin 2.1.21 (K2 compiler) |
| Target | JVM 17 |
| Source | kotlinlang.org (verified via Context7) |

---

## Routing Table

| Feature | Scope | Document |
|---|---|---|
| Null safety | `?`, `?.`, `?:`, `!!`, platform types | [null_safety.md](null_safety.md) · `[[null_safety]]` |
| Sealed types & when | exhaustive hierarchies, `data object` | [sealed_types_and_when.md](sealed_types_and_when.md) · `[[sealed_types_and_when]]` |
| Data classes | value holders, `copy()`, destructuring | [data_classes.md](data_classes.md) · `[[data_classes]]` |
| Scope functions | `let`/`run`/`with`/`apply`/`also` | [scope_functions.md](scope_functions.md) · `[[scope_functions]]` |
| Coroutines | suspend, structured concurrency, Flow | [coroutines.md](coroutines.md) · `[[coroutines]]` |
| Collections & sequences | eager vs lazy pipelines | [collections_and_sequences.md](collections_and_sequences.md) · `[[collections_and_sequences]]` |
| Extension functions | adding behavior without inheritance | [extension_functions.md](extension_functions.md) · `[[extension_functions]]` |
| Delegation | `by` for interfaces and properties | [delegation.md](delegation.md) · `[[delegation]]` |

---

## References
- [languages_orchestrator.md](../languages_orchestrator.md) · `[[languages_orchestrator]]`
- [nexus_api_orchestrator.md](../../services/nexus-api/nexus_api_orchestrator.md) · `[[nexus_api_orchestrator]]`
