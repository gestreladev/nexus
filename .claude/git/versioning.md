---
name: versioning
description: Semantic versioning strategy, release process, and changelog for Nexus.
agent:
  role: git-workflow-specialist
  tier: standard
  weight: soft
  triggers:
    - bumping a version
    - cutting a release or tag
    - writing a changelog entry
metadata:
  type: reference
---

# Versioning — Nexus

Follows **Semantic Versioning 2.0.0** (`MAJOR.MINOR.PATCH`).

| Segment | When to bump |
|---|---|
| `MAJOR` | First production release (`1.0.0`); breaking API changes after |
| `MINOR` | A roadmap phase is fully complete and merged to `main` |
| `PATCH` | Bug fix, dependency update, or improvement within a phase |

`0.x.0` = pre-production. `1.0.0` = first deployable release of the full system.

---

## Current version

`0.9.0` — released (tagged). `0.10.0` in progress (Phase 10).

## Version history

| Version | Status | Milestone | Summary |
|---|---|---|---|
| `v0.1.0` | ✅ released | First Service | Ktor scaffold, health endpoint, ModulePipeline DSL |
| `v0.2.0` | ✅ released | Database Layer | PostgreSQL, Flyway, Exposed, HikariCP |
| `v0.3.0` | ✅ released | Auth & Security | bcrypt, JWT, register/login/me, integration tests |
| `v0.4.0` | ✅ merged | Caching | Redis cache-aside + JWT denylist (Lettuce) |
| `v0.5.0` | ✅ merged | Testing & CI/CD | unit + fakes, GitHub Actions service containers |
| `v0.6.0` | ✅ merged | Python Service | `nexus-ingest` FastAPI scaffold; Python GoF tree |
| `v0.7.0` | ✅ merged | Async & Messaging | Kafka produce → consume flow |
| `v0.8.0` | ✅ merged | Containers | whole system in Docker Compose; one `up` |
| `v0.9.0` | ✅ released | Search & Vectors | pgvector + HNSW, embed Strategy, `/v1/search` |
| `v0.10.0` | 🔄 in progress | Observability | OTel tracing across services (Session A) |

---

## Release process

1. All issues for the milestone are closed.
2. PR merged into `main`.
3. `build.gradle.kts` version bumped.
4. Annotated tag: `git tag -a v0.x.0 -m "Release v0.x.0"`.
5. Tag pushed: `git push origin v0.x.0`.
6. GitHub Release created from the tag with changelog.

## Git tag convention
`v{MAJOR}.{MINOR}.{PATCH}` — annotated (`-a`), never lightweight.

## Changelog format (Keep a Changelog)
```
## [v0.2.0] — 2026-06-05
### Added
- PostgreSQL integration via Exposed DSL
### Changed
- ModulePipeline: database() stage added
```

## References
- [git_orchestrator.md](git_orchestrator.md) · `[[git_orchestrator]]`
- [milestones.md](milestones.md) · `[[milestones]]`
