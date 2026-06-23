---
name: py_observer
description: Observer pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Observer pattern in Python
metadata:
  type: pattern
---

# Observer

## Intent
Notify dependents automatically when a subject changes.

## Structure

```
Subject ──holds──> [Observer, Observer, ...]
   │  state changes
   └── notify() ──> each observer.update(event)
```

## Python example

```python
from __future__ import annotations
from dataclasses import dataclass, field
from typing import Callable

Listener = Callable[["DocStatus"], None]


@dataclass
class Document:
    status: "DocStatus" = "draft"
    _listeners: list[Listener] = field(default_factory=list)

    def subscribe(self, fn: Listener) -> Listener:
        self._listeners.append(fn)
        return fn  # usable as a decorator

    def set_status(self, status: "DocStatus") -> None:
        self.status = status
        for fn in self._listeners:
            fn(status)


DocStatus = str

doc = Document()


@doc.subscribe
def reindex(status: DocStatus) -> None:
    print(f"reindexing -> {status}")


doc.set_status("indexed")  # reindexing -> indexed
```

## When to use
- One change must fan out to several independent reactions.
- You want subjects decoupled from the observers they notify.
- Subscribers come and go at runtime (plugins, hooks, listeners).

## Python idiom
The classic `Observer` interface collapses into a plain list of callbacks invoked on
change; `subscribe` can double as a decorator since it returns the function. For async
fan-out, push events onto an `asyncio.Queue` (or yield them from an async generator)
and let consumers drain the stream independently instead of calling each observer inline.

## In Nexus
Document status transitions publish events that observers react to: when a doc moves to
`indexed`, nexus-ingest fires the change and nexus-search subscribes to refresh its index.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_mediator.md](py_mediator.md) · `[[py_mediator]]`
