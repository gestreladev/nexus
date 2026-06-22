---
name: nexus-orchestrator
description: Root orchestrator — global mandatory rules and routing for the Nexus project.
agent:
  role: claude-governor
  tier: power
  weight: heavy
  triggers:
    - starting any Nexus task
    - routing a request to the correct domain or service
    - enforcing project-wide rules
metadata:
  type: orchestrator
---

# Nexus — Root Orchestrator

Governs all content and agent behavior in this repository. Load only the files
relevant to the current request (Rule 1). Model selection for any task is
governed by [model_decision.md](configurations/model_decision.md) · `[[model_decision]]`.

## Project Metadata

| Field | Value |
|---|---|
| Name | Nexus |
| Type | Distributed AI knowledge platform |
| Purpose | Backend roadmap capstone — learning project, production-grade |
| Roadmap | https://roadmap.sh/backend |
| Repo | https://github.com/gestreladev/nexus |
| Local path | `~/Projects/backend/roadmap/nexus/` |
| GitHub account | `gestreladev` (SSH host alias: `github-gestreladev`) |
| Git remote | `git@github-gestreladev:gestreladev/nexus.git` |
| Current version | `0.2.0` |

### Services

| Service | Path | Language | Framework | Port | Status |
|---|---|---|---|---|---|
| `nexus-api` | `nexus-api/` | Kotlin 2.1.21 | Ktor 3.1.3 / Netty | 8080 | ✅ v0.2.0 |
| `nexus-ingest` | `nexus-ingest/` | Python 3.12+ | FastAPI | 8081 | ⏳ v0.6.0 |
| `nexus-search` | `nexus-search/` | Python 3.12+ | FastAPI | 8082 | ⏳ v0.11.0 |

### Infrastructure

| Component | Technology | Status |
|---|---|---|
| Relational DB | PostgreSQL 17 + pgvector (port 5433) | ✅ running |
| Cache | Redis 7 | ⏳ v0.4.0 |
| Messaging | Kafka | ⏳ v0.7.0 |
| Observability | OpenTelemetry + Grafana | ⏳ v0.10.0 |
| Container | Docker Compose (`docker-compose.yml` at repo root) | ✅ partial |

### Build tooling — nexus-api

| Tool | Version |
|---|---|
| Gradle wrapper | 8.13 |
| JDK | OpenJDK 17 (Temurin) |
| Kotlin | 2.1.21 |
| Ktor | 3.1.3 |
| Exposed | 0.61.0 |
| HikariCP | 6.3.0 |
| PostgreSQL driver | 42.7.5 |
| Flyway | 11.8.2 |
| Logback | 1.5.18 |

---

## Mandatory Rules

### Rule 1 — File Coordination (token efficiency)
Identify which `.claude/**/*.md` files are relevant before answering. Load
**only those files**, navigating the routing tables. State them (Rule 3).

### Rule 2 — 200-line limit
No `.claude/**/*.md` file may exceed **200 lines**. Propose a split before
applying it.

### Rule 3 — Context disclosure
Every response must open with:
```
📂 Context loaded: <file>, <file>   (or "none")
```
Group files by domain with a section emoji when several are loaded (🌿 Git,
🏛️ Architecture, 🔌 API, ✍️ Idioms, 🧪 Testing, 🧩 Patterns, 🗂️ Config, 📁 Service).

### Rule 4 — Model selection by tier
Map every task to a tier before starting; see
[model_decision.md](configurations/model_decision.md) · `[[model_decision]]`.
Never pre-escalate to Power without justification.

### Rule 5 — Task-weight classification + spawn limits
Classify each task Soft/Medium/Heavy before spawning sub-agents. Caps:
Soft ≤ 2, Medium ≤ 6, Heavy ≤ 16. Power spawns justified individually. See
[model_decision.md](configurations/model_decision.md) · `[[model_decision]]`.

### Rule 6 — Agent frontmatter on every file
Every `.claude/**/*.md` declares a YAML `agent:` block (role/tier/weight/
triggers + metadata.type). Roles must exist in
[agent_roles.md](configurations/agent_roles.md) · `[[agent_roles]]`.

### Rule 7 — Design-pattern consultation before code
Before writing or reviewing Kotlin/Python code, consult
[design_patterns_orchestrator.md](designpatterns/design_patterns_orchestrator.md)
· `[[design_patterns_orchestrator]]`, declare the GoF pattern used, or justify
why none applies. Any newly-introduced language must get its full GoF tree first.

### Rule 8 — Ask before proceeding
When in doubt about scope, approach, or any decision affecting architecture or
file structure — stop and ask. Never assume.

---

## Routing Table

| Domain | Scope | Document |
|---|---|---|
| Configurations | Model tiers, agent roles, how to use `.claude` | [configurations/](configurations/) · `[[claude_usage]]` |
| Git | Branching, commits, PRs, versioning, milestones | [git_orchestrator.md](git/git_orchestrator.md) · `[[git_orchestrator]]` |
| Design Patterns | GoF reference, language-routed (Kotlin now) | [design_patterns_orchestrator.md](designpatterns/design_patterns_orchestrator.md) · `[[design_patterns_orchestrator]]` |
| Fundamentals | Backend knowledge: networking, HTTP, web, databases | [fundamentals_orchestrator.md](fundamentals/fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]` |
| Services | Per-service technical docs (architecture, api, idioms, testing) | [services_orchestrator.md](services/services_orchestrator.md) · `[[services_orchestrator]]` |
| Lessons | Interactive class engine — methodology, lesson format, session protocol, logs | [lesson_orchestrator.md](lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]` |
| Project | Objective, roadmap, per-phase progress trackers, decisions | [project_develop_orchestrator.md](project/project_develop_orchestrator.md) · `[[project_develop_orchestrator]]` |

## References
- [model_decision.md](configurations/model_decision.md) · `[[model_decision]]`
- [agent_roles.md](configurations/agent_roles.md) · `[[agent_roles]]`
