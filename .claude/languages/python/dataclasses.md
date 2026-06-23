---
name: dataclasses
description: Python dataclasses — value types, frozen, and dataclass vs Pydantic.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - modeling a value/record type
    - choosing dataclass vs Pydantic
metadata:
  type: reference
---

# Dataclasses

`@dataclass` generates `__init__`, `__repr__`, `__eq__` from annotated fields —
Python's value type (the `data class` analogue).

```python
from dataclasses import dataclass, field, replace

@dataclass(frozen=True)            # immutable + hashable
class Chunk:
    text: str
    tokens: int
    tags: list[str] = field(default_factory=list)   # mutable default → factory

a = Chunk("hello world", 2)
b = replace(a, tokens=3)           # tweaked copy (Prototype) — see py_prototype
```

## Idioms
- **`frozen=True`** for immutable, hashable records (safe across async tasks).
- **`field(default_factory=list)`** — never use a mutable literal default (it's
  shared across instances, a classic bug).
- **Structural equality** for free — great in tests.
- `replace(obj, field=...)` is the idiomatic "change one field".

## dataclass vs Pydantic
| | dataclass | Pydantic `BaseModel` |
|---|---|---|
| Validation | none (hints only) | **runtime validation + coercion** |
| Use | internal domain values | API request/response boundaries |

In FastAPI you use **Pydantic** models at the HTTP edge (they validate input),
and plain dataclasses for internal domain objects.

## In Nexus
`nexus-ingest` uses dataclasses for internal `Chunk`/`Document` values and
Pydantic models for FastAPI request/response bodies.

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [py_prototype.md](../../designpatterns/python/creational/py_prototype.md) · `[[py_prototype]]`
