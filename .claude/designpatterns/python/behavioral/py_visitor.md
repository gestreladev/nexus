---
name: py_visitor
description: Visitor pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Visitor pattern in Python
metadata:
  type: pattern
---

# Visitor

## Intent
Represent an operation over the elements of an object structure. The visitor lets you define a new operation without changing the classes of the elements on which it operates.

## Structure

```
Visitor          Element
  visit_a()  <--   a.accept(v) -> v.visit_a(a)
  visit_b()  <--   b.accept(v) -> v.visit_b(b)
```

## Python example

```python
from dataclasses import dataclass
from functools import singledispatch

@dataclass
class Text:
    body: str

@dataclass
class Heading:
    body: str
    level: int

Node = Text | Heading

@singledispatch
def token_count(node: Node) -> int:
    raise NotImplementedError(node)

@token_count.register
def _(node: Text) -> int:
    return len(node.body.split())

@token_count.register
def _(node: Heading) -> int:
    return len(node.body.split()) + node.level

total = sum(token_count(n) for n in [Text("hello world"), Heading("Intro", 1)])
```

## When to use
- An object structure has many distinct, unrelated operations and you want them grouped per operation, not scattered across element classes.
- The element classes are stable but the set of operations over them keeps growing.
- You need to accumulate state while walking a heterogeneous tree (e.g. token totals, rendered output).

## Python idiom
Skip the classic `accept()` boilerplate: `functools.singledispatch` gives clean double-dispatch keyed on the first argument's type. Register one handler per node type and call the function uniformly; new operations are just new `@singledispatch` functions. When the operation must stay coupled to the structure, a plain `visit(node)` method that dispatches on `type(node).__name__` is the lighter alternative.

## In Nexus
Operations such as token count, render, and extract run over a document node tree in the Python services `nexus-ingest` and `nexus-search`. Each operation is a `singledispatch` visitor, keeping node dataclasses free of operation-specific logic.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_composite.md](../structural/py_composite.md) · `[[py_composite]]`
- [py_interpreter.md](py_interpreter.md) · `[[py_interpreter]]`
