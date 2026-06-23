---
name: py_creational_orchestrator
description: Routing for Python creational GoF patterns.
agent:
  role: pattern-router
  tier: nano
  weight: soft
  triggers:
    - selecting a creational pattern in Python
metadata:
  type: orchestrator
---

# Creational Patterns — Python

Patterns that abstract the instantiation process.

---

## Routing Table

| Pattern | Intent | Document |
|---|---|---|
| Factory Method | Defer instantiation to a callable/subclass | [py_factory_method.md](py_factory_method.md) · `[[py_factory_method]]` |
| Abstract Factory | Families of related objects | [py_abstract_factory.md](py_abstract_factory.md) · `[[py_abstract_factory]]` |
| Builder | Step-by-step construction | [py_builder.md](py_builder.md) · `[[py_builder]]` |
| Prototype | Clone existing objects | [py_prototype.md](py_prototype.md) · `[[py_prototype]]` |
| Singleton | One instance, global access | [py_singleton.md](py_singleton.md) · `[[py_singleton]]` |

---

## References
- [python_orchestrator.md](../python_orchestrator.md) · `[[python_orchestrator]]`
