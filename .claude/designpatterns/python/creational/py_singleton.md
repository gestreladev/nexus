---
name: py_singleton
description: Singleton pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Singleton pattern in Python
metadata:
  type: pattern
---

# Singleton

## Intent
Ensure one instance and a global access point.

## Structure

```
+---------------------+
|     Singleton       |
+---------------------+
| - _instance         |  <-- the one shared instance
+---------------------+
| + instance() -------|--> always returns _instance
+---------------------+
```

## Python example

```python
from __future__ import annotations
from dataclasses import dataclass, field
from functools import lru_cache


@dataclass(frozen=True)
class Settings:
    """Immutable app config; one instance per process."""
    redis_url: str = "redis://localhost:6379"
    timeout_s: float = 5.0
    tags: tuple[str, ...] = field(default_factory=tuple)


@lru_cache(maxsize=1)
def get_settings() -> Settings:
    # Built once, cached forever — the cache IS the singleton.
    return Settings()


# Same object on every call.
assert get_settings() is get_settings()
```

## When to use

- A resource is expensive to build and safe to share (config, connection pool).
- You need exactly one coordinator (a registry, a cache, a client).
- The shared state is read-mostly or internally thread-safe.

## Python idiom

A Python module **is** a singleton: it's imported once and cached in
`sys.modules`, so a module-level instance (`settings = Settings()`) is the
simplest "single instance" there is. For lazy construction, wrap a factory in
`functools.lru_cache(maxsize=1)`. A metaclass that overrides `__call__` works
too but is rarely worth the magic. Prefer passing the instance via
**dependency injection** over reaching for a global — it keeps code testable
(swap a fake in tests instead of mutating module state).

## In Nexus

Each Python service (`nexus-ingest`, `nexus-search`) holds one Redis/HTTP
client per process behind a cached factory, mirroring how `DatabaseFactory`
hands out a single pool in `nexus-api`.

## References
- [py_creational_orchestrator.md](py_creational_orchestrator.md) · `[[py_creational_orchestrator]]`
