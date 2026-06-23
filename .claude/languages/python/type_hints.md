---
name: type_hints
description: Python type hints — Optional, aliases, PEP 695 generics, static checking.
agent:
  role: language-expert
  tier: standard
  weight: medium
  triggers:
    - annotating functions or models
    - reasoning about None / generics
metadata:
  type: reference
---

# Type Hints

Python is dynamically typed, but **type hints** + a static checker (mypy /
pyright) give Kotlin-like safety *before* runtime. Hints are **not enforced at
runtime** — they're for tools, humans, and (via Pydantic/FastAPI) validation.

```python
def greet(name: str, times: int = 1) -> str:
    return (name + " ") * times
```

## "Null safety" — Optional
There's no non-null type, but the checker enforces `None` handling when you
annotate it:
```python
def find(uid: str) -> User | None:      # 3.10+ union syntax (was Optional[User])
    ...
user = find(uid)
user.email          # checker error: user may be None
if user is not None:
    user.email      # ok — narrowed
```
`x | None` is Python's nudge toward the same discipline Kotlin's `?` enforces —
*if* you run a checker. Without one, nothing stops a `None` bug; CI should run mypy.

## Aliases & generics (PEP 695, 3.12+)
```python
type Vector = list[float]               # the `type` statement
def first[T](xs: list[T]) -> T:         # generic via [T], no TypeVar boilerplate
    return xs[0]
class Box[T]: ...
```

## In Nexus
The Python services annotate everything; **Pydantic** turns hints on FastAPI
models into real request validation (the runtime teeth). `mypy` runs in CI.

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [dataclasses.md](dataclasses.md) · `[[dataclasses]]`
- [protocols_and_duck_typing.md](protocols_and_duck_typing.md) · `[[protocols_and_duck_typing]]`
