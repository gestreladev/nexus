---
name: py_factory_method
description: Factory Method pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Factory Method pattern in Python
metadata:
  type: pattern
---

# Factory Method

## Intent
Defer object creation to a callable or subclass; choose the concrete type at runtime.

## Structure
```
Creator.create() ──▶ Product (interface)
       │                  ▲
       ▼                  │
  selects key ──▶ ConcreteProductA / ConcreteProductB
```

## Python example
```python
from dataclasses import dataclass
from typing import Protocol


class Parser(Protocol):
    def parse(self, raw: bytes) -> dict: ...


@dataclass
class JsonParser:
    encoding: str = "utf-8"

    def parse(self, raw: bytes) -> dict:
        import json
        return json.loads(raw.decode(self.encoding))


@dataclass
class CsvParser:
    def parse(self, raw: bytes) -> dict:
        rows = raw.decode().splitlines()
        return {"rows": rows}


_REGISTRY: dict[str, type[Parser]] = {
    "application/json": JsonParser,
    "text/csv": CsvParser,
}


def make_parser(mime: str) -> Parser:
    try:
        return _REGISTRY[mime]()
    except KeyError:
        raise ValueError(f"no parser for {mime}") from None
```

## When to use
- The concrete class isn't known until runtime (driven by config, MIME type, or user input).
- You want callers to depend on an interface (`Protocol`/`ABC`), not a constructor.
- New variants should plug in without touching call sites.

## Python idiom
Skip the Creator hierarchy. A top-level function or dict-dispatch returning the right
class is the Pythonic Factory Method: `make_parser(mime)` looks up a `registry` keyed by
a value (or a sealed-ish `Enum`) and instantiates it. Prefer functions over classes;
reach for a decorator-based registry (`@register("text/csv")`) when variants self-register
across modules. `functools.singledispatch` is the type-driven version of the same idea.

## In Nexus
nexus-ingest uses a MIME-keyed registry to select the parser/embedder for each document,
so a new format is added by registering one class — no call-site changes in nexus-search.

## References
- [py_creational_orchestrator.md](py_creational_orchestrator.md) · `[[py_creational_orchestrator]]`
- [py_abstract_factory.md](py_abstract_factory.md) · `[[py_abstract_factory]]`
