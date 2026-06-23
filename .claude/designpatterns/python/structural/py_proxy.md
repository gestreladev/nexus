---
name: py_proxy
description: Proxy pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Proxy pattern in Python
metadata:
  type: pattern
---

# Proxy

## Intent
Provide a surrogate that controls access to another object.

## Structure

```
Client --> Subject (Protocol)
              ^
        ------+------
        |           |
   RealSubject   Proxy --> RealSubject
```

## Python example

```python
from typing import Protocol
from functools import wraps

class Service(Protocol):
    def fetch(self, key: str) -> str: ...

class RealService:
    def fetch(self, key: str) -> str:
        return f"value::{key}"

class CachingProxy:
    def __init__(self, target: Service) -> None:
        self._target = target
        self._cache: dict[str, str] = {}

    def fetch(self, key: str) -> str:
        if key not in self._cache:
            self._cache[key] = self._target.fetch(key)
        return self._cache[key]

service: Service = CachingProxy(RealService())
```

## When to use
- Add cross-cutting control (caching, lazy init, access checks) without touching the real object.
- Defer creation of an expensive resource until first use (virtual proxy).
- Guard or meter a remote/shared resource behind a uniform interface.

## Python idiom
A wrapper implementing the same `Protocol` is the natural proxy: callers stay typed against the interface and never know they hold a surrogate. For a transparent proxy that forwards everything, define `__getattr__` to delegate unhandled attribute access to the wrapped object:

```python
def __getattr__(self, name: str):
    return getattr(self._target, name)
```

Override only the methods you want to intercept; let the rest pass through.

## In Nexus
A rate-limiting/budget proxy sits in front of the LLM client, sharing the client's interface so `nexus-ingest` and `nexus-search` call it unchanged while it enforces token budgets and throttles requests.

## References
- [py_structural_orchestrator.md](py_structural_orchestrator.md) · `[[py_structural_orchestrator]]`
- [py_decorator.md](py_decorator.md) · `[[py_decorator]]`
