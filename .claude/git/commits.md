---
name: commits
description: Conventional Commits format, types, and scopes for Nexus.
agent:
  role: git-workflow-specialist
  tier: nano
  weight: soft
  triggers:
    - writing a commit message
    - checking allowed commit types or scopes
metadata:
  type: reference
---

# Commits — Nexus

All commits follow **Conventional Commits**.

```
{type}({scope}): {description}

{optional body}

Co-Authored-By: ...
```

---

## Types

| Type | When |
|---|---|
| `feat` | New feature |
| `fix` | Bug fix |
| `refactor` | Code improvement, no behaviour change |
| `chore` | Maintenance, deps, tooling |
| `docs` | Documentation |
| `test` | Tests |

## Scopes

Service or area: `nexus-api`, `nexus-ingest`, `nexus-search`, `infra`,
`claude`, `deps`.

---

## Examples

```
feat(nexus-api): add POST /v1/users endpoint
fix(nexus-api): handle duplicate email as 409 not 500
chore(deps): bump exposed to 0.62.0
docs(claude): add api/conventions.md
refactor(nexus-api): enforce plugin order via DSL
```

## Rules
1. Imperative mood ("add", not "added").
2. One logical change per commit where practical.
3. Body explains *why*, not *what* (the diff shows what).

## References
- [git_orchestrator.md](git_orchestrator.md) · `[[git_orchestrator]]`
- [branching.md](branching.md) · `[[branching]]`
