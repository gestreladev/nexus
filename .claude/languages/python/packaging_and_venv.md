---
name: packaging_and_venv
description: Python packaging — venv, pip, pyproject.toml, dependency management.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - setting up a Python project
    - managing dependencies / environments
metadata:
  type: reference
---

# Packaging & venv

## Virtual environments
Each project gets an **isolated** interpreter + dependencies, so projects don't
collide (Python's analogue to a per-project dependency set).
```bash
python -m venv .venv
source .venv/bin/activate      # Windows: .venv\Scripts\activate
python -m pip install -U pip
```
`.venv/` is gitignored; you recreate it from the lockfile, never commit it.

## pyproject.toml (the modern standard)
One declarative file for metadata + dependencies (PEP 621):
```toml
[project]
name = "nexus-ingest"
version = "0.1.0"
requires-python = ">=3.12"
dependencies = [
    "fastapi>=0.115",
    "uvicorn[standard]>=0.30",
    "pydantic>=2.7",
]

[project.optional-dependencies]
dev = ["pytest>=8", "mypy>=1.10", "ruff>=0.5"]
```

## Tooling
- **pip** — the baseline installer (`pip install -e .`).
- **uv** / **Poetry / PDM** — faster installs + lockfiles for reproducible
  builds. `uv` is the current fast favourite; pick one and pin a lockfile.
- **ruff** (lint+format), **mypy** (types), **pytest** (tests) — the standard dev set.

## In Nexus
`nexus-ingest` (and `nexus-search`) each get a `pyproject.toml` + `.venv`,
mirroring how `nexus-api` uses Gradle's version catalog — one declared, locked
dependency set per service, installed in CI.

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [twelve-factor config note in best_practices.md](../../infra/docker/best_practices.md) · `[[best_practices]]`
