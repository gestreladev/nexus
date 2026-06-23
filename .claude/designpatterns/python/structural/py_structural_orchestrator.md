---
name: py_structural_orchestrator
description: Routing for Python structural GoF patterns.
agent:
  role: pattern-router
  tier: nano
  weight: soft
  triggers:
    - selecting a structural pattern in Python
metadata:
  type: orchestrator
---

# Structural Patterns — Python

Patterns that compose objects into larger structures.

---

## Routing Table

| Pattern | Intent | Document |
|---|---|---|
| Adapter | Convert one interface to another | [py_adapter.md](py_adapter.md) · `[[py_adapter]]` |
| Bridge | Decouple abstraction from implementation | [py_bridge.md](py_bridge.md) · `[[py_bridge]]` |
| Composite | Tree structures, uniform leaf/branch | [py_composite.md](py_composite.md) · `[[py_composite]]` |
| Decorator | Add behavior dynamically | [py_decorator.md](py_decorator.md) · `[[py_decorator]]` |
| Facade | Simplified interface to a subsystem | [py_facade.md](py_facade.md) · `[[py_facade]]` |
| Flyweight | Share state to reduce memory | [py_flyweight.md](py_flyweight.md) · `[[py_flyweight]]` |
| Proxy | Surrogate controlling access | [py_proxy.md](py_proxy.md) · `[[py_proxy]]` |

---

## References
- [python_orchestrator.md](../python_orchestrator.md) · `[[python_orchestrator]]`
