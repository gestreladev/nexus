---
name: py_decorator
description: Decorator pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Decorator pattern in Python
metadata:
  type: pattern
---
# Decorator

## Intent
Attach responsibilities to an object dynamically, without subclassing, by
wrapping it in another object that shares the same interface.

## Structure
```
Component (Protocol)
   ^
   |--- ConcreteComponent        # the core object
   |--- Decorator(wrapped: Component)
            |--- LoggingDecorator
            |--- CachingDecorator   # each forwards + adds behavior
```

## Python example
```python
from __future__ import annotations
from dataclasses import dataclass
from typing import Protocol


class Repository(Protocol):
    def get(self, key: str) -> str: ...


@dataclass
class FileRepository:
    root: str

    def get(self, key: str) -> str:
        return f"{self.root}/{key}"


@dataclass
class CachingRepository:
    wrapped: Repository  # same Protocol -> transparent stand-in
    _cache: dict[str, str] = None  # type: ignore[assignment]

    def get(self, key: str) -> str:
        cache = self._cache if self._cache is not None else {}
        if key not in cache:
            cache[key] = self.wrapped.get(key)
        self._cache = cache
        return cache[key]


repo: Repository = CachingRepository(FileRepository(root="/data"))
print(repo.get("user-42"))  # cached on second call
```

## When to use
- You want to add behavior (caching, logging, retry) to individual objects, not a whole class.
- Subclassing would cause a combinatorial explosion of variants.
- Responsibilities should be addable and removable at runtime.

## Python idiom
For functions, use Python's `@decorator` syntax with `functools.wraps` to
preserve the wrapped callable's name and docstring:

```python
import functools

def logged(fn):
    @functools.wraps(fn)
    def wrapper(*args, **kwargs):
        print(f"call {fn.__name__}")
        return fn(*args, **kwargs)
    return wrapper
```

For objects, wrap an instance that implements the same `Protocol` and forward
the calls you don't override. Either way the wrapper is interchangeable with
the original.

## In Nexus
In `nexus-ingest` and `nexus-search`, caching, retry, and logging are layered
as decorators around a repository or the LLM client, so each concern stays
independent and composable without touching the core implementation.

## References
- [py_structural_orchestrator.md](py_structural_orchestrator.md) · `[[py_structural_orchestrator]]`
- [py_proxy.md](py_proxy.md) · `[[py_proxy]]`
