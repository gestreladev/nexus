---
name: py_command
description: Command pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Command pattern in Python
metadata:
  type: pattern
---

# Command

## Intent
Encapsulate a request as an object to queue, log, or undo it.

## Structure

```
Invoker ──> Command (execute / undo)
                │
                └──> Receiver (does the real work)
```

## Python example

```python
from __future__ import annotations
from dataclasses import dataclass
from typing import Protocol


class Command(Protocol):
    def execute(self) -> None: ...
    def undo(self) -> None: ...


@dataclass(frozen=True)
class AddTag:
    doc_id: str
    tag: str
    store: dict[str, set[str]]

    def execute(self) -> None:
        self.store.setdefault(self.doc_id, set()).add(self.tag)

    def undo(self) -> None:
        self.store.get(self.doc_id, set()).discard(self.tag)


def run(commands: list[Command]) -> None:
    for cmd in commands:
        cmd.execute()
```

## When to use
- You need to queue, schedule, or replay requests asynchronously.
- You want an audit log or undo/redo history of operations.
- You want to decouple the caller (invoker) from the worker (receiver).

## Python idiom
The lightest form is just a callable: a plain function or `functools.partial(do_work, arg)` *is* a command, no class needed. When you also need `undo`, reach for a frozen `@dataclass` carrying the arguments plus paired `execute()` / `undo()` methods — the dataclass gives you a hashable, comparable, loggable record for free.

## In Nexus
Async ingestion jobs are encapsulated as command objects placed on a Kafka queue and run by consumers in the Python services `nexus-ingest` and `nexus-search`. This lets ingestion be retried, replayed, and audited independently of the producer.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
