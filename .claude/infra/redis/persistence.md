---
name: persistence
description: Redis persistence — RDB snapshots vs AOF append-only.
agent:
  role: infra-specialist
  tier: standard
  weight: soft
  triggers:
    - deciding Redis durability
    - configuring RDB/AOF
metadata:
  type: reference
---

# Redis Persistence

Redis is in-memory, but can persist to disk so data survives a restart. Two
mechanisms (verified via Context7 / redis.io):

| Mode | How | Trade-off |
|---|---|---|
| **RDB** | point-in-time snapshots at intervals | compact, fast restart; loses writes since the last snapshot |
| **AOF** | append every write to a log, replay on start | far less data loss; larger files, slower restart |
| **RDB + AOF** | both (default-ish in modern Redis) | snapshot for fast load + AOF for durability |
| **none** | pure cache | fastest; data is disposable |

## Choosing by role
- **Cache data** → persistence optional; it can be rebuilt from PostgreSQL. RDB
  (or none) is fine.
- **Denylist / sessions** → these must survive a restart, or revoked tokens
  become valid again and users get logged out. Use **AOF** (or accept that a
  restart clears the denylist and rely on short token TTLs).

## In Nexus
Dev uses Redis defaults. For the denylist's correctness, enable AOF in the
compose config (or keep token TTLs short so a flush is low-impact) — a decision
recorded when the denylist ships.

## References
- [redis_orchestrator.md](redis_orchestrator.md) · `[[redis_orchestrator]]`
- [revocation_and_denylist.md](../../security/jwt/revocation_and_denylist.md) · `[[revocation_and_denylist]]`
