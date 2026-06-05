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
| **Current version** | `0.2.0-dev` |

### Services

| Service | Path | Language | Framework | Port | Status |
|---|---|---|---|---|---|
| `nexus-api` | `nexus-api/` | Kotlin 2.1.21 | Ktor 3.1.3 / Netty | 8080 | ✅ v0.1.0 |
| `nexus-ingest` | `nexus-ingest/` | Python 3.12+ | FastAPI | 8081 | ⏳ v0.6.0 |
| `nexus-search` | `nexus-search/` | Python 3.12+ | FastAPI | 8082 | ⏳ v0.11.0 |

### Infrastructure

| Component | Technology | Status |
|---|---|---|
| Relational DB | PostgreSQL 17 + pgvector (port 5433) | ✅ running |
| Cache | Redis 7 | ⏳ v0.4.0 |
| Messaging | Kafka | ⏳ v0.7.0 |
| Observability | OpenTelemetry + Grafana | ⏳ v0.10.0 |
| Container | Docker Compose | ✅ partial |

### Build tooling

| Tool | Version | Scope |
|---|---|---|
| Gradle wrapper | 8.13 | `nexus-api` |
| JDK | OpenJDK 17 (Temurin) | JVM runtime |
| Version catalog | `gradle/libs.versions.toml` | All JVM deps |

### Conventions

| Concern | Rule |
|---|---|
| Branching | `feat/<scope>` or `fix/<scope>` off `main`; PR required |
| Commits | Conventional Commits: `feat`, `fix`, `refactor`, `chore`, `test`, `docs` |
| API versioning | All routes under `/v1/` |
| Error shape | `{ "error": "CODE", "message": "..." }` only |
| Secrets | `.env` gitignored; `.env.example` documents all vars |
| Quality | Production-grade always — typed, tested, no hardcoded secrets |

---

## Mandatory Rules

### Rule 1 — File Coordination (token efficiency)
Identify which `.claude/**/*.md` files are relevant before answering.
Load **only those files**. State them (Rule 3). If none apply, note the gap.

### Rule 2 — 200-line limit
No `.claude/**/*.md` file may exceed **200 lines**. Propose a split to
the user before applying it.

### Rule 3 — Transparency
Every response must open with:
```
📂 Context loaded: <file>, <file>   (or "none")
```

### Rule 4 — Ask before proceeding
When in doubt about scope, approach, or any decision affecting architecture
or file structure — **stop and ask**. Never assume.

### Rule 5 — Model selection
- **Planning** (architecture, lesson design, roadmap decisions) → `claude-opus-4-7`
- **Implementation** (writing code, creating files, running commands) → `claude-sonnet-4-6`

---

## Knowledge Index

| File | Load when… |
|---|---|
| `architecture/designpatterns/chain_of_responsibility.md` | Handler chain, pipeline, middleware, ordered processing |
| `idioms/dsl.md` | Kotlin DSL, `@DslMarker`, builder patterns, extension-function APIs |
| `git/versioning.md` | Version bumps, release process, changelog, git tags, milestone planning |
