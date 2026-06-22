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

`0.2.0` — released.

## Version history

| Version | Status | Milestone | Summary |
|---|---|---|---|
| `v0.1.0` | ✅ released | First Service | Ktor scaffold, health endpoint, ModulePipeline DSL |
| `v0.2.0` | ✅ released | Database Layer | PostgreSQL, Flyway, Exposed, HikariCP |

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
