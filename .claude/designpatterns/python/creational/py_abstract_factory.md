---
name: py_abstract_factory
description: Abstract Factory pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Abstract Factory pattern in Python
metadata:
  type: pattern
---

# Abstract Factory

## Intent
Create families of related objects without naming concrete classes.

## Structure
```
AbstractFactory          ConcreteFactory ──► ProductA, ProductB
   create_a() ─┐               ▲
   create_b() ─┘          (one family per factory)
client(factory) ──► uses products via abstract interfaces only
```

## Python example
```python
from dataclasses import dataclass
from typing import Protocol


class Embedder(Protocol):
    def embed(self, text: str) -> list[float]: ...


class VectorStore(Protocol):
    def upsert(self, vector: list[float]) -> None: ...


@dataclass(frozen=True)
class AIStack:
    embedder: Embedder
    store: VectorStore


def hosted_stack() -> AIStack:
    return AIStack(OpenAIEmbedder(), PineconeStore())


def local_stack() -> AIStack:
    return AIStack(SentenceTransformer(), FAISSStore())


STACKS = {"hosted": hosted_stack, "local": local_stack}


def build_stack(name: str) -> AIStack:
    return STACKS[name]()  # whole family wired together
```

## When to use
- You need to swap a whole family of collaborating objects at once.
- Client code should depend on interfaces, never concrete classes.
- Mixing products from different families must be prevented.

## Python idiom
Skip the class hierarchy: define product interfaces as `typing.Protocol` (structural, no inheritance needed), then let each factory be a plain function returning a small frozen `@dataclass` holder. The dict registry maps a name to a factory function, so adding a family is one entry — no abstract base class boilerplate.

## In Nexus
nexus-ingest and nexus-search call `build_stack("hosted")` in production and `build_stack("local")` in tests or offline runs, swapping embedder, vector store, and LLM client together without touching call sites.

## References
- [py_creational_orchestrator.md](py_creational_orchestrator.md) · `[[py_creational_orchestrator]]`
- [py_factory_method.md](py_factory_method.md) · `[[py_factory_method]]`
