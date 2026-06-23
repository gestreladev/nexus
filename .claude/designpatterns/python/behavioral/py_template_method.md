---
name: py_template_method
description: Template Method pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Template Method pattern in Python
metadata:
  type: pattern
---

# Template Method

## Intent
Fix an algorithm skeleton, deferring some steps to subclasses.

## Structure
```
AbstractClass
  template_method()   # fixed skeleton, calls steps
  step_a()            # abstract — subclass fills in
  step_b()            # abstract — subclass fills in

ConcreteClass(AbstractClass)
  step_a(), step_b()  # concrete overrides
```

## Python example
```python
from abc import ABC, abstractmethod
from typing import final

class Pipeline(ABC):
    @final
    def run(self, raw: str) -> list[str]:
        parsed = self.parse(raw)          # fixed skeleton
        return self.chunk(parsed)         # steps deferred

    @abstractmethod
    def parse(self, raw: str) -> str: ...

    @abstractmethod
    def chunk(self, text: str) -> list[str]: ...

class MarkdownPipeline(Pipeline):
    def parse(self, raw: str) -> str:
        return raw.strip()

    def chunk(self, text: str) -> list[str]:
        return [b for b in text.split("\n\n") if b]
```

## When to use
- The overall algorithm is stable but a few steps vary by case.
- You want to avoid duplicating the invariant control flow across variants.
- Subclasses should only fill in well-defined holes, not reorder the flow.

## Python idiom
Use an `abc.ABC` with `@abstractmethod` step methods and one `@final` template
method that orchestrates them — `@final` signals the skeleton is fixed. When the
variation is just one or two callables, skip the class hierarchy and pass step
functions into a plain template function instead.

## In Nexus
The ingest workflow fixes the skeleton (parse to chunk to store) while each format
supplies its own parse/chunk overrides, as in nexus-ingest. nexus-search reuses the
same shape so every backend follows one invariant flow.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_strategy.md](py_strategy.md) · `[[py_strategy]]`
