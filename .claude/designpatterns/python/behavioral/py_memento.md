---
name: py_memento
description: Memento pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Memento pattern in Python
metadata:
  type: pattern
---

# Memento

## Intent
Capture and restore an object state without breaking encapsulation.

## Structure
```
Originator --save()--> Memento (frozen snapshot)
Caretaker  stores [Memento, Memento, ...]
Originator <-restore(Memento)-- Caretaker
```

## Python example
```python
from __future__ import annotations
import copy
from dataclasses import dataclass, field, replace


@dataclass(frozen=True)
class Memento:
    state: dict[str, object]


@dataclass
class Draft:
    body: dict[str, object] = field(default_factory=dict)

    def save(self) -> Memento:
        return Memento(state=copy.deepcopy(self.body))

    def restore(self, memento: Memento) -> None:
        self.body = copy.deepcopy(memento.state)


draft = Draft({"title": "v1"})
checkpoint = draft.save()
draft.body["title"] = "v2"
draft.restore(checkpoint)
assert draft.body["title"] == "v1"
```

## When to use
- You need undo/rollback or checkpointing of an object's state.
- The state must stay private — callers should not poke at internals.
- Snapshots are taken before a risky or reversible operation.

## Python idiom
Model the snapshot as an immutable `@dataclass(frozen=True)`, so a stored
checkpoint can never be mutated after capture. For nested or mutable state,
use `copy.deepcopy` when saving and restoring so the memento and the live
object never share references.

## In Nexus
`nexus-ingest` captures a frozen Draft checkpoint before re-ingestion, so a
failed or unwanted run can be rolled back; `nexus-search` reuses the same
snapshots to restore index state.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_prototype.md](../creational/py_prototype.md) · `[[py_prototype]]`
