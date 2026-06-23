---
name: py_state
description: State pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the State pattern in Python
metadata:
  type: pattern
---

# State

## Intent
Let an object alter behavior when its internal state changes — the object appears to change class.

## Structure
```
Context --> State (Protocol)
              ^
   pending / processing / ready / failed
each State.handle(ctx) -> next State
```

## Python example
```python
from __future__ import annotations
from dataclasses import dataclass, field
from typing import Protocol


class State(Protocol):
    def advance(self, ctx: "Document") -> "State": ...


class Pending:
    def advance(self, ctx: "Document") -> State:
        return Processing()


class Processing:
    def advance(self, ctx: "Document") -> State:
        return Ready() if ctx.indexed else Failed()


class Ready:
    def advance(self, ctx: "Document") -> State:
        return self  # terminal


class Failed:
    def advance(self, ctx: "Document") -> State:
        return self  # terminal


@dataclass
class Document:
    indexed: bool = False
    state: State = field(default_factory=Pending)

    def tick(self) -> None:
        self.state = self.state.advance(self)
```

## When to use
- An object's behavior depends on a mode that changes at runtime.
- You have sprawling conditionals branching on a status field.
- Transitions and their legality need to be explicit and testable.

## Python idiom
Drop the class-per-state ceremony when states are simple: use an `Enum` plus a dispatch dict mapping each state to a transition function. When behavior is rich, small classes implementing a `typing.Protocol` work well — each `advance` returns the next state object rather than mutating shared flags, keeping transitions pure and easy to unit-test.

## In Nexus
The `documents.status` lifecycle (`pending` to `processing` to `ready`/`failed`) is a State machine driven by the Python services `nexus-ingest` and `nexus-search`, where each step returns the next status rather than scattering `if status ==` checks across handlers.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_strategy.md](py_strategy.md) · `[[py_strategy]]`
