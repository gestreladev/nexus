---
name: py_facade
description: Facade pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Facade pattern in Python
metadata:
  type: pattern
---

# Facade

## Intent
Provide a simplified interface to a subsystem, hiding the wiring of several collaborators behind one entry point.

## Structure

```
Client --> Facade
              |--> Parser
              |--> Chunker
              |--> Embedder
              |--> Store
```

## Python example

```python
from dataclasses import dataclass, field
from typing import Protocol


class Parser(Protocol):
    def parse(self, raw: bytes) -> str: ...


class Embedder(Protocol):
    def embed(self, text: str) -> list[float]: ...


@dataclass
class IngestionFacade:
    parser: Parser
    embedder: Embedder
    store: dict[str, list[float]] = field(default_factory=dict)

    def ingest(self, doc_id: str, raw: bytes) -> None:
        text = self.parser.parse(raw)
        self.store[doc_id] = self.embedder.embed(text)
```

## When to use

- A client needs only the common path through a tangled subsystem.
- You want to decouple callers from the order and identity of internal steps.
- You are wrapping a legacy or third-party API behind a stable surface.

## Python idiom
A plain class (or even a single function) that composes injected collaborators. Keep it thin: the facade orchestrates calls and adds no business logic of its own. Inject dependencies via `__init__` or `@dataclass` fields and type them with `typing.Protocol` so the subsystem stays swappable and testable.

## In Nexus
An `IngestionFacade` in `nexus-ingest` exposes parse to chunk to embed to store as one `ingest()` call, so `nexus-search` and callers never touch the individual stages.

## References
- [py_structural_orchestrator.md](py_structural_orchestrator.md) · `[[py_structural_orchestrator]]`
- [py_adapter.md](py_adapter.md) · `[[py_adapter]]`
