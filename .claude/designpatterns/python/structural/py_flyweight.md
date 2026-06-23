---
name: py_flyweight
description: Flyweight pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Flyweight pattern in Python
metadata:
  type: pattern
---

# Flyweight

## Intent
Share immutable state to support many fine-grained objects efficiently.

## Structure

```
FlyweightFactory --pool--> {key: Flyweight}   # cached, shared
       |                         ^
   get(key) ------reuses--------/
Client ---uses---> Flyweight(intrinsic) + extrinsic state passed at call time
```

## Python example

```python
from dataclasses import dataclass
from functools import lru_cache


@dataclass(frozen=True, slots=True)
class TextStyle:  # intrinsic, shared state
    font: str
    size: int
    bold: bool


@lru_cache(maxsize=None)  # the flyweight pool
def get_style(font: str, size: int, bold: bool) -> TextStyle:
    return TextStyle(font, size, bold)


def render(glyphs: list[tuple[str, TextStyle]]) -> None:
    for char, style in glyphs:  # extrinsic char + shared style
        print(f"{char!r} @ {style.font}/{style.size}")


a = get_style("Inter", 12, False)
b = get_style("Inter", 12, False)
assert a is b  # same cached instance
```

## When to use
- You hold huge numbers of objects that mostly differ by a small extrinsic part.
- The shareable (intrinsic) state is immutable and cheap to key on.
- Memory pressure dominates and identity-based reuse pays off.

## Python idiom
Skip a hand-rolled factory. Cache a frozen `@dataclass` constructor with
`@functools.lru_cache` (or a plain `dict` pool) so equal keys return the same
instance. For strings specifically, `sys.intern("token")` deduplicates them into
one shared object, making `is` comparisons fast and shrinking memory.

## In Nexus
Intern repeated tokens and metadata keys when processing large corpora in
nexus-ingest / nexus-search, so millions of records share one object per value.

## References
- [py_structural_orchestrator.md](py_structural_orchestrator.md) · `[[py_structural_orchestrator]]`
