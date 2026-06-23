---
name: comprehensions_and_generators
description: Python comprehensions and generators — eager vs lazy pipelines.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - transforming a collection
    - streaming large data lazily
metadata:
  type: reference
---

# Comprehensions & Generators

## Comprehensions (eager)
Build a new collection in one expression:
```python
emails = [u.email for u in users if u.active]      # list
by_id  = {u.id: u for u in users}                  # dict
tags   = {t for u in users for t in u.tags}        # set
```
Readable and fast for small/medium data; materializes the whole result.

## Generators (lazy)
A **generator expression** `(... )` or a `yield` function produces items one at a
time — nothing is computed until iterated. Best for large or streaming data.
```python
stripped = (line.strip() for line in huge_file)    # generator expression

def infinite(start: int) -> Iterator[int]:          # generator function
    while True:
        yield start
        start += 1
```
`sum(x * x for x in range(10_000))` never builds the list — constant memory.

## Choosing
| Use | When |
|---|---|
| comprehension `[...]` | small/medium, need a real list |
| generator `(...)` / `yield` | large data, streaming, or short-circuit (`next`, `any`) |

`itertools` (`chain`, `islice`, `groupby`) composes generators without
materializing. This is Python's analogue to Kotlin sequences.

## In Nexus
Chunk/embedding pipelines in `nexus-ingest` stream documents through generators
so a large corpus never loads fully into memory.

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [py_iterator.md](../../designpatterns/python/behavioral/py_iterator.md) · `[[py_iterator]]`
