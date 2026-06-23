---
name: py_iterator
description: Iterator pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Iterator pattern in Python
metadata:
  type: pattern
---

# Iterator

## Intent
Access elements sequentially without exposing the underlying structure.

## Structure

```
Client --> Iterable.__iter__() --> Iterator
                                      |
                                      v
                              __next__() -> item ... StopIteration
```

## Python example

```python
from collections.abc import Iterator
from dataclasses import dataclass, field


@dataclass
class Page:
    rows: list[str]
    next_cursor: str | None


@dataclass
class PagedFeed:
    fetch: "callable"  # cursor -> Page
    _cursor: str | None = field(default=None)

    def __iter__(self) -> Iterator[str]:
        cursor = self._cursor
        while True:
            page = self.fetch(cursor)
            yield from page.rows
            if page.next_cursor is None:
                return
            cursor = page.next_cursor
```

## When to use
- You need to traverse a collection without revealing how it is stored.
- The sequence is large, remote, or infinite and should stay lazy.
- Multiple, independent traversals over the same source are required.
- You want a uniform `for`-loop interface over heterogeneous backends.

## Python idiom
A generator function (using `yield`) implements the iterator protocol for free:
calling it returns an object with `__iter__`/`__next__` already wired, and
`StopIteration` is raised when the function returns. Reach for it instead of
hand-writing a class with explicit state. For composition, lean on `itertools`
(`islice`, `chain`, `takewhile`, `groupby`) to slice, join, and window streams
without buffering. `yield from` cleanly delegates to a sub-iterator.

## In Nexus
Stream paged DB and search results lazily so `nexus-ingest` and `nexus-search`
process records one page at a time instead of materializing the full result set
in memory.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
