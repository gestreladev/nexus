---
name: kotlin-orchestrator
description: Kotlin GoF design-pattern routing by category.
agent:
  role: pattern-router
  tier: standard
  weight: soft
  triggers:
    - selecting a Kotlin GoF pattern
    - routing a Kotlin pattern query by category
metadata:
  type: orchestrator
---

# Kotlin Design Patterns

GoF patterns with idiomatic Kotlin implementations. Many GoF patterns simplify
in Kotlin thanks to first-class functions, `object` singletons, `sealed`
hierarchies, delegation (`by`), and extension functions — each leaf notes the
idiomatic shortcut where one exists.

---

## Category routing

| Category | Document |
|---|---|
| Creational | [creational_orchestrator.md](creational/creational_orchestrator.md) · `[[creational_orchestrator]]` |
| Structural | [structural_orchestrator.md](structural/structural_orchestrator.md) · `[[structural_orchestrator]]` |
| Behavioral | [behavioral_orchestrator.md](behavioral/behavioral_orchestrator.md) · `[[behavioral_orchestrator]]` |

---

## References
- [design_patterns_orchestrator.md](../design_patterns_orchestrator.md) · `[[design_patterns_orchestrator]]`
- [kotlin_language_orchestrator.md](../../languages/kotlin/kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]` — the language itself (features/idioms)
