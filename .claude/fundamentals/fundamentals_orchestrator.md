---
name: fundamentals_orchestrator
description: Routing for backend fundamentals — the language-agnostic knowledge taught in lessons.
agent:
  role: fundamentals-router
  tier: standard
  weight: medium
  triggers:
    - looking up a backend concept (HTTP, ACID, normalization, etc.)
    - grounding a lesson in durable reference knowledge
metadata:
  type: orchestrator
---

# Fundamentals Orchestrator — Nexus

The durable, language-agnostic backend knowledge taught across lessons. This is
**the knowledge itself** — distinct from `lessons/` (how we teach) and
`services/nexus-api/` (how we implemented it). Each leaf links back to the
lesson that taught it and the code that applies it.

Model selection governed by
[model_decision.md](../configurations/model_decision.md) · `[[model_decision]]`.

---

## Area routing

| Area | Scope | Lessons | Document |
|---|---|---|---|
| Networking | Internet, DNS, TCP, connections | 1 | [networking_orchestrator.md](networking/networking_orchestrator.md) · `[[networking_orchestrator]]` |
| HTTP | Methods, status, headers, idempotency, HTTPS/TLS | 1 | [http_orchestrator.md](http/http_orchestrator.md) · `[[http_orchestrator]]` |
| Web | Web servers, REST, OpenAPI | 2 | [web_orchestrator.md](web/web_orchestrator.md) · `[[web_orchestrator]]` |
| Databases | Relational model, ACID, normalization, indexing, migrations, ORM, NoSQL | 3 | [databases_orchestrator.md](databases/databases_orchestrator.md) · `[[databases_orchestrator]]` |
| Caching | Cache-aside, invalidation, TTL/eviction, stampede, HTTP caching | 5 | [caching_orchestrator.md](caching/caching_orchestrator.md) · `[[caching_orchestrator]]` |
| Messaging | Brokers, Kafka, delivery/ordering, real-time (WS/SSE) | 8 | [messaging_orchestrator.md](messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]` |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [lesson_orchestrator.md](../lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]`
- [roadmap.md](../project/roadmap.md) · `[[roadmap]]`
