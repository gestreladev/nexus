# Versioning

## Strategy

Nexus follows **Semantic Versioning 2.0.0** (`MAJOR.MINOR.PATCH`).

| Segment | When to bump |
|---|---|
| `MAJOR` | First production-ready release (`1.0.0`); breaking API changes after that |
| `MINOR` | A roadmap phase is fully complete and merged to `main` |
| `PATCH` | Bug fix, dependency update, or improvement within a phase |

`0.x.0` signals the project is pre-production. `1.0.0` is the first
deployable release of the complete Nexus system.

## Current version

`0.2.0-dev` — Database layer in progress (`feat/phase-2-database`)

## Version history

| Version | Status | Milestone | Summary |
|---|---|---|---|
| `v0.1.0` | ✅ released | First Service | Ktor scaffold, health endpoint, ModulePipeline DSL |
| `v0.2.0` | 🔄 in progress | Database Layer | PostgreSQL, Flyway, Exposed, HikariCP |

## Release process

1. All issues for the milestone are closed
2. PR merged into `main`
3. `build.gradle.kts` version bumped
4. Git tag created: `git tag -a v0.x.0 -m "Release v0.x.0"`
5. Tag pushed: `git push origin v0.x.0`
6. GitHub Release created from the tag with changelog

## Git tag convention

```
v{MAJOR}.{MINOR}.{PATCH}
```

Examples: `v0.1.0`, `v0.2.0`, `v1.0.0`

Tags are annotated (`-a`), never lightweight.

## Changelog format

Each release entry follows **Keep a Changelog** (keepachangelog.com):

```
## [v0.2.0] — 2026-06-05
### Added
- PostgreSQL integration via Exposed DSL
- Flyway migrations: users, documents tables
- HikariCP connection pool as pipeline stage
### Changed
- ModulePipeline: database() stage added between statusPages and routing
```

## Branch → version mapping

| Branch pattern | Version impact |
|---|---|
| `feat/*` | targets a `MINOR` bump on merge |
| `fix/*` | targets a `PATCH` bump on merge |
| `chore/*` | targets a `PATCH` bump on merge |
