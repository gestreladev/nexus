---
name: python_language_orchestrator
description: Routing for Python language features and idioms (the language itself).
agent:
  role: language-expert
  tier: standard
  weight: medium
  triggers:
    - using a Python language feature
    - writing idiomatic Python in nexus-ingest / nexus-search
metadata:
  type: orchestrator
---

# Python — Language Orchestrator

Reference for the **Python language** as used in the Python services
(`nexus-ingest`, `nexus-search`). Pinned to **Python 3.13** (current stable;
the vault's `3.12+`). For GoF patterns in Python see
[python_orchestrator.md](../../designpatterns/python/python_orchestrator.md) · `[[python_orchestrator]]`.
Model selection: [model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Metadata

| Field | Value |
|---|---|
| Version | Python 3.13 (stable; 3.14 newer) |
| Type checking | static via mypy / pyright (hints aren't enforced at runtime) |
| Async | `asyncio` (FastAPI is async-first) |
| Source | docs.python.org (verified via Context7) |

---

## Routing Table

| Feature | Scope | Document |
|---|---|---|
| Type hints | `X | None`, aliases, PEP 695 generics, checkers | [type_hints.md](type_hints.md) · `[[type_hints]]` |
| Dataclasses | `@dataclass`, frozen, vs Pydantic | [dataclasses.md](dataclasses.md) · `[[dataclasses]]` |
| Match statement | structural pattern matching | [match_statement.md](match_statement.md) · `[[match_statement]]` |
| Async / await | coroutines, `asyncio`, `gather`, `async with` | [async_await.md](async_await.md) · `[[async_await]]` |
| Comprehensions & generators | eager vs lazy pipelines | [comprehensions_and_generators.md](comprehensions_and_generators.md) · `[[comprehensions_and_generators]]` |
| Decorators | `@decorator`, `functools.wraps` | [decorators.md](decorators.md) · `[[decorators]]` |
| Context managers | `with`, `__enter__/__exit__`, `@contextmanager` | [context_managers.md](context_managers.md) · `[[context_managers]]` |
| Protocols & duck typing | structural typing, ABCs | [protocols_and_duck_typing.md](protocols_and_duck_typing.md) · `[[protocols_and_duck_typing]]` |
| Packaging & venv | `venv`, `pip`, `pyproject.toml` | [packaging_and_venv.md](packaging_and_venv.md) · `[[packaging_and_venv]]` |

---

## References
- [languages_orchestrator.md](../languages_orchestrator.md) · `[[languages_orchestrator]]`
- [python_orchestrator.md](../../designpatterns/python/python_orchestrator.md) · `[[python_orchestrator]]`
