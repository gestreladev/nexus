# Nexus — Claude Orchestrator

You are working on **Nexus**, a distributed AI knowledge platform built as a
backend learning roadmap capstone. All decisions must follow the files indexed
below. Load only the files relevant to the current prompt — do not load all
files at once.

---

## Mandatory Rules

### Rule 1 — File Coordination (token efficiency)
Before answering any prompt, identify which `.claude/**/*.md` files are
relevant. Load **only those files**. State them explicitly (see Rule 3).
If no file applies, answer from general knowledge and note the gap.

### Rule 2 — 200-line limit
Every markdown file under `.claude/` must never exceed **200 lines**.
If a file approaches the limit, split it. Propose the split to the user
before applying it.

### Rule 3 — Transparency
At the start of every response, output a block like:

```
📂 Context loaded: <file1>, <file2>   (or "none" if not applicable)
```

This must appear before any explanation or code.

### Rule 4 — Ask before proceeding
When in doubt about scope, approach, or a decision that affects architecture
or file structure — **stop and ask**. Do not assume. One focused question
beats a wrong implementation.

---

## Knowledge Index

### Architecture

| File | Load when… |
|---|---|
| `architecture/designpatterns/chain_of_responsibility.md` | Implementing or reviewing a handler chain, pipeline, middleware, or request-processing sequence |

### Idioms

| File | Load when… |
|---|---|
| `idioms/dsl.md` | Writing, reviewing, or explaining Kotlin DSL constructs, `@DslMarker`, builder patterns, or extension-function-based APIs |

---

## Project Snapshot

- **Repo:** https://github.com/gestreladev/nexus
- **Active service:** `nexus-api` (Kotlin / Ktor 3.1.3)
- **Current phase:** Phase 1 — first service scaffold
- **Branch convention:** `feat/<scope>`, PR into `main` (protected)
- **Commit convention:** Conventional Commits (`feat`, `fix`, `refactor`, `chore`, `test`, `docs`)
- **Non-negotiable:** production-grade, up-to-date, best practices always
