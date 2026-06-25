---
name: decisions
description: ADR-style log of key Nexus decisions.
agent:
  role: progress-tracker
  tier: standard
  weight: soft
  triggers:
    - recording an architectural or process decision
    - checking why a past choice was made
metadata:
  type: reference
---

# Decision Log — Nexus

Lightweight ADRs. Newest last. Each: decision, why, status.

| # | Decision | Why | Status |
|---|---|---|---|
| 1 | Capstone = "Nexus" AI knowledge platform | Naturally requires every roadmap topic; aligns with AI-integration goal | ✅ |
| 2 | Kotlin for services, Python for AI/data, SQL throughout | Right tool per job; learner already fluent in Kotlin | ✅ |
| 3 | Ktor (not Spring) for `nexus-api` | Lighter, idiomatic Kotlin, good for learning the layers explicitly | ✅ |
| 4 | Gradle wrapper 8.13 | Ktor shadow-plugin incompat with Gradle 9 | ✅ |
| 5 | PostgreSQL image = `pgvector/pgvector:pg17` | Avoids a later migration when vectors arrive (Phase 9) | ✅ |
| 6 | Exposed DSL mode (not DAO) | Keeps persistence out of domain objects | ✅ |
| 7 | Semver milestones (v0.1.0…v1.0.0), one per phase | Real release discipline vs phase-name milestones | ✅ |
| 8 | `.claude` mirrors vault-omniverse recursive-orchestrator pattern | Consistent governance; token-efficient routing | ✅ |
| 9 | Full GoF tree per language in use (Kotlin now) | Rule 7; new languages get their tree before code | ✅ |
| 10 | 3-tier model selection (Nano/Standard/Power) | Adopted from vault; replaces ad-hoc Rule 5 | ✅ |
| 11 | Vault is single source of truth (PLAN.md → pointer) | Repo self-contained; no drift | ✅ |
| 12 | Multi-stage, non-root container baseline for all services | Small images + reduced attack surface; runtime ships only the artifact | ✅ |
| 13 | Kafka **dual-listener** (EXTERNAL `localhost:9092` + INTERNAL `kafka:29092`) | A broker advertises an address back to clients; one address can't serve both host and in-network containers | ✅ |
| 14 | Compose secrets via required `${VAR:?}` from a gitignored `.env` | No plaintext secrets committed to a public repo; fail fast if unset | ✅ |
| 15 | Embedding provider behind a Strategy (local + Voyage + fake), selected by env | Swap providers without code change; fake enables offline/CI testing (Rule 7) | ✅ |
| 16 | Standardize embeddings on **1024 dims** for every provider | A common dim makes local↔Voyage interchangeable with no re-embed migration | ✅ |
| 17 | `documents` stores `content`; `nexus-api` Flyway owns all schema (incl. `document_chunks`) | Claim-check: thin events, DB is the system of record; single migration authority | ✅ |
| 18 | Fat jar must `mergeServiceFiles()` | Shadow-merge clobbered Flyway's `ServiceLoader` plugin file → SQL resolver dropped → migrations silently un-resolved | ✅ |

## How to add
Append a row with the next number. If a decision is reversed, add a new row
referencing the old one rather than editing history.

## References
- [project_develop_orchestrator.md](project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
