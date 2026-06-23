---
name: py_behavioral_orchestrator
description: Routing for Python behavioral GoF patterns.
agent:
  role: pattern-router
  tier: nano
  weight: soft
  triggers:
    - selecting a behavioral pattern in Python
metadata:
  type: orchestrator
---

# Behavioral Patterns — Python

Patterns concerned with algorithms and the assignment of responsibilities.

---

## Routing Table

| Pattern | Intent | Document |
|---|---|---|
| Chain of Responsibility | Pass a request along a handler chain | [py_chain_of_responsibility.md](py_chain_of_responsibility.md) · `[[py_chain_of_responsibility]]` |
| Command | Encapsulate a request as an object | [py_command.md](py_command.md) · `[[py_command]]` |
| Interpreter | Evaluate sentences in a language | [py_interpreter.md](py_interpreter.md) · `[[py_interpreter]]` |
| Iterator | Sequential access without exposing structure | [py_iterator.md](py_iterator.md) · `[[py_iterator]]` |
| Mediator | Centralize complex communication | [py_mediator.md](py_mediator.md) · `[[py_mediator]]` |
| Memento | Capture and restore state | [py_memento.md](py_memento.md) · `[[py_memento]]` |
| Observer | Notify dependents of state changes | [py_observer.md](py_observer.md) · `[[py_observer]]` |
| State | Alter behavior when state changes | [py_state.md](py_state.md) · `[[py_state]]` |
| Strategy | Interchangeable algorithms | [py_strategy.md](py_strategy.md) · `[[py_strategy]]` |
| Template Method | Skeleton with overridable steps | [py_template_method.md](py_template_method.md) · `[[py_template_method]]` |
| Visitor | Operations on an object structure | [py_visitor.md](py_visitor.md) · `[[py_visitor]]` |

---

## References
- [python_orchestrator.md](../python_orchestrator.md) · `[[python_orchestrator]]`
