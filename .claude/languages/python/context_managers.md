---
name: context_managers
description: Python context managers — with, __enter__/__exit__, @contextmanager.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - managing a resource (connection, file, lock)
    - guaranteeing cleanup
metadata:
  type: reference
---

# Context Managers

`with` guarantees setup/teardown around a block — the resource is released even
on exception. Python's structured answer to "acquire then always release".

```python
with open("doc.txt") as f:      # __enter__ returns f; __exit__ closes it
    data = f.read()
# f is closed here, even if read() raised
```

## Writing one — the generator way
The idiomatic shortcut is `@contextlib.contextmanager`: code before `yield` is
`__enter__`, after is `__exit__`.
```python
from contextlib import contextmanager

@contextmanager
def db_session(pool):
    conn = pool.acquire()
    try:
        yield conn              # the `as` value
    finally:
        pool.release(conn)      # always runs

with db_session(pool) as conn:
    conn.execute(...)
```

## Class form
Implement `__enter__` / `__exit__` directly when you need state on the manager.
`__exit__(exc_type, exc, tb)` can suppress an exception by returning `True`.

## Async
`async with` + `__aenter__`/`__aexit__` for async resources (async DB/HTTP
sessions) — common in FastAPI.

## In Nexus
DB connections, Redis clients, and file handles in the Python services are
acquired via `with`/`async with`, so cleanup is guaranteed — the analogue of
Kotlin's `transaction { }` boundary discipline.

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [async_await.md](async_await.md) · `[[async_await]]`
