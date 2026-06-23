---
name: decorators
description: Python decorators — wrapping functions, functools.wraps.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - adding cross-cutting behavior to a function
    - understanding @decorator syntax
metadata:
  type: reference
---

# Decorators

A decorator is a callable that takes a function and returns a replacement —
`@name` is sugar for `f = name(f)`. Python's first-class way to layer
cross-cutting concerns (the Decorator pattern, built into the language).

```python
import functools, time

def timed(fn):
    @functools.wraps(fn)                 # preserve name/docstring/signature
    def wrapper(*args, **kwargs):
        start = time.perf_counter()
        try:
            return fn(*args, **kwargs)
        finally:
            print(f"{fn.__name__}: {time.perf_counter() - start:.3f}s")
    return wrapper

@timed
def embed(text: str) -> list[float]: ...
```

## Key points
- **`functools.wraps`** — always use it, or the wrapped function loses its
  `__name__`, docstring, and signature (breaks introspection/docs).
- **`*args, **kwargs`** — forward arbitrary arguments transparently.
- **Decorators with arguments** = a decorator *factory* (one more nesting level).
- Common uses: caching (`@functools.lru_cache`), retries, auth, timing, routing
  (FastAPI's `@app.get(...)` is a decorator).

## In Nexus
FastAPI route handlers in `nexus-ingest` are registered via `@app.post(...)`;
custom decorators add retry/timing around embedding and LLM calls.

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [py_decorator.md](../../designpatterns/python/structural/py_decorator.md) · `[[py_decorator]]`
