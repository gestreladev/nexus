---
name: phase_00
description: Phase 0 tracker — setup (internet, http, git, github).
agent:
  role: progress-tracker
  tier: nano
  weight: soft
  triggers:
    - reviewing Phase 0 status
metadata:
  type: reference
---

# Phase 0 — Setup

| Field | Value |
|---|---|
| Version | — |
| Lesson | 1 |
| Topics | how-internet-works, http, https, dns, version-control, git, github |
| Status | ✅ complete |

## Deliverables
- [x] `gestreladev/nexus` repo created
- [x] Monorepo structure (`nexus-api/`, `nexus-ingest/`, `nexus-search/`, `infra/`, `docs/`)
- [x] `main` branch protection
- [x] Commit + branch conventions

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [lesson_01_foundations.md](../../lessons/log/lesson_01_foundations.md) · `[[lesson_01_foundations]]`
