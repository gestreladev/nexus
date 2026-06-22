---
name: agent-roles
description: Full role-to-responsibility mapping for agents operating in Nexus.
agent:
  role: claude-governor
  tier: nano
  weight: soft
  triggers:
    - looking up a role definition
    - validating a frontmatter role value
metadata:
  type: reference
---

# Agent Role Reference

Full mapping of agent roles to responsibilities. Referenced from
[CLAUDE.md](../CLAUDE.md) Rule 6. Every `.claude/**/*.md` `agent.role` value
must be one of these.

---

| Role | Responsibility | Default tier |
|---|---|---|
| `claude-governor` | Project-wide governance and routing | power |
| `model-selector` | Selecting the correct model tier for a task | standard |
| `service-router` | Routing requests to the correct service docs | standard |
| `ktor-specialist` | Ktor/Kotlin backend — routing, plugins, pipeline | standard |
| `database-specialist` | PostgreSQL, Exposed, Flyway, schema, transactions | standard |
| `api-specialist` | REST design, versioning, status codes, response shapes | standard |
| `fastapi-specialist` | Python services — FastAPI, async (future) | standard |
| `ai-specialist` | RAG, embeddings, LLM integration, MCP (future) | power |
| `messaging-specialist` | Kafka producers/consumers, async pipeline | standard |
| `caching-specialist` | Redis, cache strategies, invalidation | standard |
| `git-workflow-specialist` | Branching, commits, PRs, tags, releases | standard |
| `infra-specialist` | Docker, Compose, CI/CD, environments | standard |
| `test-specialist` | Unit/integration tests, fakes, testApplication | standard |
| `code-reviewer` | PR review across all layers | standard |
| `security-auditor` | Auth, OWASP, secrets, threat modeling | power |
| `pattern-router` | Routing GoF design-pattern queries | standard |
| `pattern-advisor` | Explaining and applying GoF design patterns | standard |
| `curriculum-architect` | Lesson methodology, structure, assessment design | power |
| `tutor` | Delivering an interactive lesson; routing learning requests | standard |
| `assessor` | Grading exercises, mastery checkpoints | standard |
| `progress-tracker` | Roadmap, phase, and progress tracking toward the objective | standard |
| `reference-reader` | Read-only informational lookups | nano |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [model_decision.md](model_decision.md) · `[[model_decision]]`
