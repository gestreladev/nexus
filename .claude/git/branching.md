---
name: branching
description: Branch naming, strategy, and protection rules for Nexus.
agent:
  role: git-workflow-specialist
  tier: nano
  weight: soft
  triggers:
    - naming a branch
    - checking branch conventions or protection rules
metadata:
  type: reference
---

# Branching — Nexus

Simplified **GitHub Flow**: `main` is always deployable and protected; all work
happens on short-lived branches merged via PR.

```
main ──────────────●──────────────●──────────
                  ↑               ↑
           feat/phase-2     fix/health-timeout
```

---

## Branch naming

```
{type}/{short-description}
```

| Type | When |
|---|---|
| `feat/` | New feature or phase deliverable |
| `fix/` | Bug fix |
| `chore/` | Dependency bump, tooling, config |
| `docs/` | Documentation only |
| `test/` | Adding or fixing tests only |
| `refactor/` | Code improvement, no behaviour change |

Examples:
```
feat/phase-2-database
fix/health-endpoint-500
chore/bump-ktor-3.2
refactor/pipeline-error-handling
```

Lowercase, hyphenated. No ticket numbers — milestones and labels carry context.

---

## Rules

1. Always branch from `main`.
2. One concern per branch — do not mix unrelated changes.
3. Branches are deleted after merge.

## Protection on `main`
- Force pushes: blocked
- Deletions: blocked
- PR required before merge

## References
- [git_orchestrator.md](git_orchestrator.md) · `[[git_orchestrator]]`
- [commits.md](commits.md) · `[[commits]]`
- [pull_requests.md](pull_requests.md) · `[[pull_requests]]`
