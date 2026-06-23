---
name: py_interpreter
description: Interpreter pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Interpreter pattern in Python
metadata:
  type: pattern
---

# Interpreter

## Intent
Define a grammar and evaluate sentences in a small language.

## Structure
```
Expr (ABC)
 ├─ Lit          eval() -> value
 ├─ And(l, r)    eval() -> l.eval() & r.eval()
 └─ Or(l, r)     eval() -> l.eval() | r.eval()
        ↑ each node interprets itself against a context
```

## Python example
```python
from __future__ import annotations
from abc import ABC, abstractmethod
from dataclasses import dataclass

Context = dict[str, str]

class Expr(ABC):
    @abstractmethod
    def eval(self, ctx: Context) -> bool: ...

@dataclass(frozen=True, slots=True)
class Match(Expr):          # field:value, e.g. status:ready
    field: str
    value: str
    def eval(self, ctx: Context) -> bool:
        return ctx.get(self.field) == self.value

@dataclass(frozen=True, slots=True)
class And(Expr):
    left: Expr
    right: Expr
    def eval(self, ctx: Context) -> bool:
        return self.left.eval(ctx) and self.right.eval(ctx)

# status:ready AND tag:py
query = And(Match("status", "ready"), Match("tag", "py"))
assert query.eval({"status": "ready", "tag": "py"})
```

## When to use
- A simple, stable grammar recurs often (filters, rules, formulas).
- Sentences are worth representing as a tree you can evaluate or transform.
- The language is small; a full parser/compiler would be overkill.

## Python idiom
Model the grammar as a sealed tree of frozen `@dataclass` nodes, each with an `eval()` method dispatched by `abc.ABC` (or `match`/`isinstance`). For anything resembling a real language, skip hand-rolling and lean on the stdlib `ast` module to parse and walk Python-like syntax instead.

## In Nexus
A document-filter mini-language (`status:ready AND tag:py`) lets nexus-search compile user queries into an `Expr` tree, which nexus-ingest reuses to test documents at index time.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_visitor.md](py_visitor.md) · `[[py_visitor]]`
