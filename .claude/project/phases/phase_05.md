---
name: phase_05
description: Phase 5 tracker — testing & CI/CD.
agent:
  role: progress-tracker
  tier: nano
  weight: soft
  triggers:
    - reviewing or planning Phase 5
metadata:
  type: reference
---

# Phase 5 — Testing & CI/CD

| Field | Value |
|---|---|
| Version | `v0.5.0` ⏳ |
| Lesson | 6 |
| Topics | testing, unit-testing, integration-testing, functional-testing, ci--cd |
| Status | ⏳ planned |

## Planned deliverables
- [ ] Unit tests for routes (testApplication + fakes)
- [ ] Integration tests for repositories against a test PostgreSQL
- [ ] GitHub Actions: build + test on PR
- [ ] Coverage reporting

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [testing_strategy.md](../../services/nexus-api/testing/testing_strategy.md) · `[[testing_strategy]]`
