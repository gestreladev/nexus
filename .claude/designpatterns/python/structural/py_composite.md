---
name: py_composite
description: Composite pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Composite pattern in Python
metadata:
  type: pattern
---

# Composite

## Intent
Compose objects into trees; treat leaves and branches uniformly.

## Structure

```
        Component (abstract: token_count())
        /                      \
   Leaf (Paragraph)      Composite (Section)
                              holds: list[Component]
                              delegates recursively
```

## Python example

```python
from __future__ import annotations
from abc import ABC, abstractmethod
from dataclasses import dataclass, field


class Node(ABC):
    @abstractmethod
    def token_count(self) -> int: ...


@dataclass
class Paragraph(Node):  # leaf
    text: str

    def token_count(self) -> int:
        return len(self.text.split())


@dataclass
class Section(Node):  # composite
    title: str
    children: list[Node] = field(default_factory=list)

    def token_count(self) -> int:
        own = len(self.title.split())
        return own + sum(child.token_count() for child in self.children)
```

## When to use
- A part-whole hierarchy exists and clients should ignore the leaf/branch distinction.
- Recursive aggregation (counts, sizes, rendering) over a tree.
- Tree shape varies at runtime and you want uniform traversal.

## Python idiom
A shared `abc.ABC` (or `typing.Protocol`) declares one recursive method, and the
composite implements it with a generator-fed `sum(...)`/`any(...)` over its
children. For simple trees you can skip classes entirely and recurse over nested
`list`/`dict` literals.

## In Nexus
In `nexus-ingest`, a document `Section` tree aggregates token counts recursively
so chunking splits on real budgets; `nexus-search` walks the same tree to score
matched branches.

## References
- [py_structural_orchestrator.md](py_structural_orchestrator.md) · `[[py_structural_orchestrator]]`
