---
name: web_orchestrator
description: Routing for web-layer fundamentals (Lesson 2).
agent:
  role: fundamentals-router
  tier: nano
  weight: soft
  triggers:
    - looking up a web-server, REST, or OpenAPI concept
metadata:
  type: orchestrator
---

# Web — Fundamentals

Serving HTTP at the application layer. Taught in Lesson 2.

---

## Routing Table

| Topic | Scope | Document |
|---|---|---|
| Web servers | App server vs reverse proxy, Nginx | [web_servers.md](web_servers.md) · `[[web_servers]]` |
| REST | Constraints, resource modeling, versioning | [rest.md](rest.md) · `[[rest]]` |
| OpenAPI | Machine-readable API contracts | [openapi.md](openapi.md) · `[[openapi]]` |

---

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [conventions.md](../../services/nexus-api/api/conventions.md) · `[[conventions]]`
- [lesson_02_first_service.md](../../lessons/log/lesson_02_first_service.md) · `[[lesson_02_first_service]]`
