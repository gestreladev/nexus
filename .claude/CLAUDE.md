# Nexus — Claude Orchestrator

## Project Metadata

| Field | Value |
|---|---|
| **Name** | Nexus |
| **Type** | Distributed AI knowledge platform |
| **Purpose** | Backend roadmap capstone — learning project, production-grade |
| **Roadmap** | https://roadmap.sh/backend |
| **Repo** | https://github.com/gestreladev/nexus |
| **Local path** | `~/Projects/backend/roadmap/nexus/` |
| **GitHub account** | `gestreladev` (SSH host alias: `github-gestreladev`) |
| **Git remote** | `git@github-gestreladev:gestreladev/nexus.git` |

### Services

| Service | Path | Language | Framework | Port | Status |
|---|---|---|---|---|---|
| `nexus-api` | `nexus-api/` | Kotlin 2.1.21 | Ktor 3.1.3 / Netty | 8080 | ✅ scaffolded |
| `nexus-ingest` | `nexus-ingest/` | Python 3.12+ | FastAPI | 8081 | ⏳ planned |
| `nexus-search` | `nexus-search/` | Python 3.12+ | FastAPI | 8082 | ⏳ planned |

### Infrastructure (planned)

| Component | Technology | Purpose |
|---|---|---|
| Relational DB | PostgreSQL 17 + pgvector | Users, documents, embeddings |
| Cache | Redis 7 | Session cache, hot queries |
| Messaging | Kafka | Async ingestion pipeline |
| Observability | OpenTelemetry + Grafana | Logs, metrics, traces |
| Container | Docker Compose | Local full-system runtime |

### Build tooling

| Tool | Version | Scope |
|---|---|---|
| Gradle wrapper | 8.13 | `nexus-api` build |
| JDK | OpenJDK 17 (Temurin) | JVM runtime |
| Version catalog | `gradle/libs.versions.toml` | All JVM dependencies |

### Conventions

| Concern | Rule |
|---|---|
| Branching | `feat/<scope>` off `main`; PR required; `main` is protected |
| Commits | Conventional Commits: `feat`, `fix`, `refactor`, `chore`, `test`, `docs` |
| API versioning | All routes under `/v1/` from day one |
| Error responses | `{ "error": "CODE", "message": "..." }` — no other shape |
| Config | `application.yaml` + env vars; no hardcoded secrets ever |
| Secrets | `.env` is gitignored; `.env.example` documents required vars |
| Quality | Production-grade always — typed responses, error handling, tests |

### Current phase

**Phase 1 — First Service** (`feat/phase-0-setup` open as PR #1)
Next: Phase 2 — PostgreSQL + data layer

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
Every response must open with:
```
📂 Context loaded: <file1>, <file2>   (or "none" if not applicable)
```

### Rule 4 — Ask before proceeding
When in doubt about scope, approach, or a decision that affects architecture
or file structure — **stop and ask**. Never assume.

---

## Knowledge Index

| File | Load when… |
|---|---|
| `architecture/designpatterns/chain_of_responsibility.md` | Handler chain, pipeline, middleware, or ordered processing |
| `idioms/dsl.md` | Kotlin DSL, `@DslMarker`, builder patterns, extension-function APIs |
