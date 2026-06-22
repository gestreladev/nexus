---
name: networking_orchestrator
description: Routing for networking fundamentals (Lesson 1).
agent:
  role: fundamentals-router
  tier: nano
  weight: soft
  triggers:
    - looking up a networking concept
metadata:
  type: orchestrator
---

# Networking — Fundamentals

How bytes get from a client to `nexus-api`. Taught in Lesson 1.

---

## Routing Table

| Topic | Scope | Document |
|---|---|---|
| How the internet works | Request lifecycle, layers, IP | [how_the_internet_works.md](how_the_internet_works.md) · `[[how_the_internet_works]]` |
| DNS | Resolution, record types, TTL | [dns.md](dns.md) · `[[dns]]` |
| TCP & connections | Handshake, keep-alive, pooling | [tcp_and_connections.md](tcp_and_connections.md) · `[[tcp_and_connections]]` |

---

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [lesson_01_foundations.md](../../lessons/log/lesson_01_foundations.md) · `[[lesson_01_foundations]]`
