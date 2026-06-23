---
name: py_chain_of_responsibility
description: Chain of Responsibility pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Chain of Responsibility pattern in Python
metadata:
  type: pattern
---

# Chain of Responsibility

## Intent
Give several objects a chance to handle a request along a chain. The request travels from handler to handler until one of them processes it, decoupling the sender from the concrete receiver.

## Structure
```
request --> [Handler A] --handled? no--> [Handler B] --no--> [Handler C] --> result
                 |                            |                   |
              handle()                     handle()            handle()
```

## Python example
```python
from dataclasses import dataclass
from typing import Callable, Iterable, Optional

@dataclass
class Request:
    user: str
    amount: int

# A handler returns a result, or None to pass the request along.
Handler = Callable[[Request], Optional[str]]

def deny_anonymous(req: Request) -> Optional[str]:
    return "denied: anonymous" if not req.user else None

def cap_amount(req: Request) -> Optional[str]:
    return "denied: over limit" if req.amount > 1000 else None

def approve(req: Request) -> Optional[str]:
    return f"approved {req.amount} for {req.user}"

def process(req: Request, chain: Iterable[Handler]) -> str:
    return next((r for h in chain if (r := h(req)) is not None), "unhandled")

print(process(Request("ana", 500), [deny_anonymous, cap_amount, approve]))
```

## When to use
- Multiple handlers may process a request and the right one is decided at runtime.
- You want to add, reorder, or remove processing steps without touching callers.
- The set of handlers and their order should be configurable or data-driven.

## Python idiom
The textbook linked-list of handler objects is rarely needed. The idiom is a plain list of handler callables iterated until one returns a non-`None` result (a generator with the walrus operator and `next()` does this cleanly). Linked handlers still fit when each stage must mutate-and-forward. FastAPI middleware and dependencies are this pattern: each layer may short-circuit or delegate to `call_next`.

## In Nexus
The nexus-ingest pipeline is a chain of stages (parse, dedup, embed) where any stage can reject or transform a document; nexus-search composes a FastAPI middleware chain for auth, rate limiting, and tracing on each request.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_command.md](py_command.md) · `[[py_command]]`
