---
name: py_builder
description: Builder pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Builder pattern in Python
metadata:
  type: pattern
---

# Builder

## Intent
Separate construction of a complex object from its representation, so the same
construction process can build different representations step by step.

## Structure
```
Builder ──set_a()──┐
        ──set_b()──┤ (each returns self, fluent chaining)
        ──build()──┴──> Product (validated, immutable)
```

## Python example
```python
from __future__ import annotations
from dataclasses import dataclass, field
from typing import Self


@dataclass(frozen=True)
class QueryConfig:
    terms: tuple[str, ...]
    limit: int
    filters: dict[str, str]


class QueryBuilder:
    def __init__(self) -> None:
        self._terms: list[str] = []
        self._limit: int = 10
        self._filters: dict[str, str] = {}

    def term(self, value: str) -> Self:
        self._terms.append(value)
        return self

    def limit(self, n: int) -> Self:
        self._limit = n
        return self

    def filter(self, key: str, value: str) -> Self:
        self._filters[key] = value
        return self

    def build(self) -> QueryConfig:
        if not self._terms:
            raise ValueError("query needs at least one term")
        return QueryConfig(tuple(self._terms), self._limit, dict(self._filters))


config = QueryBuilder().term("python").filter("lang", "en").limit(25).build()
```

## When to use
- Construction takes many steps or optional parameters that arrive over time.
- The object must be validated or frozen only once fully assembled.
- The same steps should yield different representations or variants.

## Python idiom
Usually replaced by `@dataclass` + keyword/default args, which already give you
named, optional, defaulted fields in a single readable call. Reach for a fluent
builder only when construction is genuinely staged (parameters trickle in across
code paths) or needs validation at `build()` time before the object exists.

## In Nexus
In `nexus-search`, build a search `QueryConfig` step by step — accumulating terms,
filters, and limits across request handlers before validating and freezing it.

## References
- [py_creational_orchestrator.md](py_creational_orchestrator.md) · `[[py_creational_orchestrator]]`
- [py_prototype.md](py_prototype.md) · `[[py_prototype]]`
