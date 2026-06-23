---
name: py_strategy
description: Strategy pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Strategy pattern in Python
metadata:
  type: pattern
---

# Strategy

## Intent
Define interchangeable algorithms and select one at runtime.

## Structure
```
Context ──> Strategy (interface)
              ▲     ▲
              │     │
        StrategyA  StrategyB   (concrete algorithms)
```

## Python example
```python
from dataclasses import dataclass
from typing import Protocol


class Compressor(Protocol):
    def compress(self, data: bytes) -> bytes: ...


@dataclass
class Archive:
    strategy: Compressor

    def store(self, data: bytes) -> bytes:
        return self.strategy.compress(data)


def gzip_compress(data: bytes) -> bytes:
    import gzip
    return gzip.compress(data)


# A bare callable satisfies a single-method Protocol.
print(Archive(strategy=gzip_compress).store(b"payload"))  # type: ignore[arg-type]
```

## When to use
- Several variants of one algorithm differ only in behavior, not interface.
- You want to swap behavior at runtime without `if/elif` ladders.
- Callers should stay agnostic to which concrete algorithm runs.
- New variants should be addable without touching the context.

## Python idiom
The idiomatic shortcut is to skip the class hierarchy entirely: a strategy is
just a function, and functions are first-class objects you can pass around and
store. Reach for a `typing.Protocol` only when a strategy needs more than one
method; for a single operation, a plain callable (or `Callable[..., R]` hint) is
the most natural and lightest-weight Python pattern.

## In Nexus
Chunking in `nexus-ingest` and ranking in `nexus-search` are pluggable strategy
functions, so each service can swap algorithms without rewiring its pipeline.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_state.md](py_state.md) · `[[py_state]]`
