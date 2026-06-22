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
| Current version | `0.3.0` |

### Services

| Service | Path | Language | Framework | Port | Status |
|---|---|---|---|---|---|
| `nexus-api` | `nexus-api/` | Kotlin 2.1.21 | Ktor 3.1.3 / Netty | 8080 | ✅ v0.3.0 |
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

### Rule 9 — Keep related docs in sync as lessons progress
Whenever a lesson or development task introduces or changes a concept, every
related `.claude` doc must be updated **in the same change** — never deferred:
- the lesson `log/` entry (answers, gaps, status)
- `project/progress.md` + the relevant `project/phases/phase_NN.md`
- `lessons/glossary.md` (new terms)
- any `fundamentals/`, `services/`, `designpatterns/`, or `languages/` doc whose
  subject was taught or changed
- this orchestrator's metadata (version, current phase) when it moves

A change that teaches something but leaves the vault stale is incomplete. See
[session_protocol.md](lessons/session_protocol.md) · `[[session_protocol]]` for
the close-of-session checklist.

### Rule 10 — Explain every PR's files after opening
Immediately after opening any PR, walk through **every file changed in that PR**,
line by line, explaining each in detail — before the PR is reviewed or merged.
- Cover all changed files (code, config, and `.claude` docs), not a subset.
- Go in dependency/readable order; explain the intent of each meaningful line or
  block, not just a summary.
- Use the PR diff as the source of truth (`gh pr diff <n>` / `git diff`), not memory.
- The PR does not proceed to merge until its files have been explained.

See [pull_requests.md](git/pull_requests.md) · `[[pull_requests]]` for where this
fits in the PR workflow.

---

## Routing Table

| Domain | Scope | Document |
|---|---|---|
| Configurations | Model tiers, agent roles, how to use `.claude` | [configurations/](configurations/) · `[[claude_usage]]` |
| Git | Branching, commits, PRs, versioning, milestones | [git_orchestrator.md](git/git_orchestrator.md) · `[[git_orchestrator]]` |
| Design Patterns | GoF reference, language-routed (Kotlin now) | [design_patterns_orchestrator.md](designpatterns/design_patterns_orchestrator.md) · `[[design_patterns_orchestrator]]` |
| Fundamentals | Backend knowledge: networking, HTTP, web, databases | [fundamentals_orchestrator.md](fundamentals/fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]` |
| Languages | Per-language feature/idiom reference (Kotlin now) | [languages_orchestrator.md](languages/languages_orchestrator.md) · `[[languages_orchestrator]]` |
| Infrastructure | Docker, Compose, Redis, containerization (CI/CD, proxy later) | [infra_orchestrator.md](infra/infra_orchestrator.md) · `[[infra_orchestrator]]` |
| Security | Auth, JWT, password hashing, OWASP, TLS, CORS | [security_orchestrator.md](security/security_orchestrator.md) · `[[security_orchestrator]]` |
| Services | Per-service technical docs (architecture, api, idioms, testing) | [services_orchestrator.md](services/services_orchestrator.md) · `[[services_orchestrator]]` |
| Lessons | Interactive class engine — methodology, lesson format, session protocol, logs | [lesson_orchestrator.md](lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]` |
| Project | Objective, roadmap, per-phase progress trackers, decisions | [project_develop_orchestrator.md](project/project_develop_orchestrator.md) · `[[project_develop_orchestrator]]` |

## References
- [model_decision.md](configurations/model_decision.md) · `[[model_decision]]`
- [agent_roles.md](configurations/agent_roles.md) · `[[agent_roles]]`
