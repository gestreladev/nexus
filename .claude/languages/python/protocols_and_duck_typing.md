---
name: protocols_and_duck_typing
description: Python duck typing and typing.Protocol — structural typing.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - defining an interface / port
    - typing duck-typed code
metadata:
  type: reference
---

# Protocols & Duck Typing

**Duck typing** — "if it quacks like a duck…": Python cares whether an object has
the right methods, not its class. So you often need *no* interface at all.

```python
def total_tokens(nodes):           # any object with .tokens works
    return sum(n.tokens for n in nodes)
```

## typing.Protocol — typed duck typing (structural)
When you want the checker to verify "has these methods" *without* inheritance,
use `Protocol` (PEP 544). A class conforms just by having the members — no
`implements`, no base class.
```python
from typing import Protocol

class VectorStore(Protocol):
    def upsert(self, id: str, vec: list[float]) -> None: ...
    def query(self, vec: list[float], k: int) -> list[str]: ...

def index(store: VectorStore, ...) -> None:    # any conforming object passes
    ...
```
This is the idiomatic way to define a "port" in Python — structural, not nominal.

## Protocol vs ABC
- **`Protocol`** — structural; conformance is implicit (preferred for ports/seams).
- **`abc.ABC`** + `@abstractmethod` — nominal; subclasses must inherit and are
  checked at instantiation. Use when you also want shared base behavior.

## In Nexus
Domain ports in the Python services (vector store, embedder, LLM client) are
`Protocol`s, so adapters conform structurally — keeping vendor types out of the
domain (see py_adapter).

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [type_hints.md](type_hints.md) · `[[type_hints]]`
- [py_adapter.md](../../designpatterns/python/structural/py_adapter.md) · `[[py_adapter]]`
