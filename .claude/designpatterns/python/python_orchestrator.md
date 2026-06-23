---
name: python_orchestrator
description: Python GoF design-pattern routing by category.
agent:
  role: pattern-router
  tier: standard
  weight: soft
  triggers:
    - selecting a Python GoF pattern
    - routing a Python pattern query by category
metadata:
  type: orchestrator
---

# Python Design Patterns

GoF patterns with idiomatic Python implementations. Many GoF patterns collapse
in Python thanks to first-class functions, duck typing, `@dataclass`, `abc.ABC`
/ `Protocol`, decorators, generators, and `functools` — each leaf flags the
idiomatic shortcut where one exists. Files are `py_`-prefixed to keep wikilinks
unique alongside the Kotlin tree.

For Python's language features (not patterns), see
`languages/python/` (added with `nexus-ingest`, Phase 6).

---

## Category routing

| Category | Document |
|---|---|
| Creational | [py_creational_orchestrator.md](creational/py_creational_orchestrator.md) · `[[py_creational_orchestrator]]` |
| Structural | [py_structural_orchestrator.md](structural/py_structural_orchestrator.md) · `[[py_structural_orchestrator]]` |
| Behavioral | [py_behavioral_orchestrator.md](behavioral/py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]` |

---

## References
- [design_patterns_orchestrator.md](../design_patterns_orchestrator.md) · `[[design_patterns_orchestrator]]`
- [kotlin_orchestrator.md](../kotlin/kotlin_orchestrator.md) · `[[kotlin_orchestrator]]` — the Kotlin tree (compare idioms)
