---
name: http_orchestrator
description: Routing for HTTP fundamentals (Lesson 1).
agent:
  role: fundamentals-router
  tier: nano
  weight: soft
  triggers:
    - looking up an HTTP concept
metadata:
  type: orchestrator
---

# HTTP — Fundamentals

The protocol `nexus-api` speaks. Taught in Lesson 1.

---

## Routing Table

| Topic | Scope | Document |
|---|---|---|
| HTTP basics | Methods, status codes, headers | [http_basics.md](http_basics.md) · `[[http_basics]]` |
| Idempotency & statelessness | Safe retries, where state lives | [idempotency_and_statelessness.md](idempotency_and_statelessness.md) · `[[idempotency_and_statelessness]]` |
| HTTPS & TLS | Encryption, authentication, integrity | [https_tls.md](https_tls.md) · `[[https_tls]]` |

---

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [conventions.md](../../services/nexus-api/api/conventions.md) · `[[conventions]]`
- [lesson_01_foundations.md](../../lessons/log/lesson_01_foundations.md) · `[[lesson_01_foundations]]`
