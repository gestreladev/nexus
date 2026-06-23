---
name: py_prototype
description: Prototype pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Prototype pattern in Python
metadata:
  type: pattern
---

# Prototype

## Intent
Create new objects by cloning an existing one.

## Structure
```
Prototype (clone) ──> ConcretePrototype
       │                     │
   client ──copies──> new instance (independent state)
```

## Python example
```python
from __future__ import annotations
import copy
from dataclasses import dataclass, field, replace


@dataclass
class QueryConfig:
    index: str
    filters: dict[str, str] = field(default_factory=dict)
    limit: int = 10

    def clone(self, **overrides) -> "QueryConfig":
        # deepcopy guards the mutable `filters` dict, then apply tweaks
        fresh = copy.deepcopy(self)
        return replace(fresh, **overrides) if overrides else fresh


base = QueryConfig(index="docs", filters={"lang": "en"})
per_request = base.clone(limit=50)        # independent copy
per_request.filters["tenant"] = "acme"    # base.filters untouched
```

## When to use
- Building an object from scratch is costly (heavy I/O, deep nesting, validation).
- You need many near-identical variants differing in a few fields.
- The concrete class is decided at runtime and you only hold a sample instance.

## Python idiom
Python ships Prototype for free. Use `copy.copy(obj)` for a shallow clone and
`copy.deepcopy(obj)` when nested mutable state must be independent. For
dataclasses, `dataclasses.replace(obj, field=...)` returns a tweaked copy in
one line — clone-plus-override without a custom method. Reach for `deepcopy`
first only when mutable containers are shared.

## In Nexus
Clone a base `QueryConfig` per request instead of rebuilding it each time, so
nexus-ingest and nexus-search start from a validated prototype and just override
the few fields that vary.

## References
- [py_creational_orchestrator.md](py_creational_orchestrator.md) · `[[py_creational_orchestrator]]`
- [py_builder.md](py_builder.md) · `[[py_builder]]`
