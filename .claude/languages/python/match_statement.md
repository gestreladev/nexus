---
name: match_statement
description: Python structural pattern matching — match/case.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - branching on the shape/type of data
    - modeling a closed set of cases
metadata:
  type: reference
---

# Match Statement

`match`/`case` (3.10+) is **structural pattern matching** — Python's closest
analogue to Kotlin's `sealed` + `when`. It matches on *shape*, not just value.

```python
def describe(node: Node) -> str:
    match node:
        case Text(body=b):                 # capture by structure
            return f"text: {len(b.split())} words"
        case Heading(level=1):             # match + literal
            return "h1"
        case Heading():
            return "heading"
        case _:                            # wildcard
            return "unknown"
```

## What you can match
- **Classes** by structure: `Point(x=0, y=y)` (captures `y`).
- **Sequences**: `case [first, *rest]:`.
- **Mappings**: `case {"type": "doc", "id": id}:`.
- **Literals / OR**: `case 200 | 201:`.
- **Guards**: `case Heading(level=l) if l > 3:`.

## Caveat — not exhaustive by default
Unlike Kotlin's `when` over a sealed type, Python does **not** force you to cover
every case. Add `case _:` or rely on a checker; a missing case silently falls
through. Discipline + mypy fill the gap.

## In Nexus
Dispatching on document node shape (text vs heading vs image) and on ingest
event/result types reads cleanly as `match`.

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [py_visitor.md](../../designpatterns/python/behavioral/py_visitor.md) · `[[py_visitor]]`
