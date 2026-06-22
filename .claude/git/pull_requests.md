---
name: pull-requests
description: PR titles, templates, merge strategy, and label rules for Nexus.
agent:
  role: git-workflow-specialist
  tier: standard
  weight: soft
  triggers:
    - opening a pull request
    - choosing a PR template or labels
    - deciding merge strategy
metadata:
  type: reference
---

# Pull Requests — Nexus

---

## Rules

1. **One concern per PR.** Auth + a bug fix are two PRs.
2. **Title follows Conventional Commits** — `feat(nexus-api): add user endpoint`.
3. **Every PR** has a milestone and at least one `type:*` label.
4. **Squash merge** into `main` — linear, readable history.

---

## Templates

Four templates live in `.github/PULL_REQUEST_TEMPLATE/`:

| Template | For |
|---|---|
| `feature.md` | `feat/*` — roadmap topics, acceptance criteria, `.claude` sync |
| `fix.md` | `fix/*` — problem, root cause, fix, verification, side effects |
| `chore.md` | `chore/*` — what/why, tooling-table sync |
| `docs.md` | `docs/*` — 200-line + Knowledge Index + metadata checks |

GitHub shows **no automatic picker** for multiple templates — select one by
appending `?template=<name>.md` to the PR compare URL.

---

## Labels

| Group | Labels |
|---|---|
| `type:*` | `feat` `fix` `refactor` `chore` `docs` `test` `lesson` |
| `service:*` | `nexus-api` `nexus-ingest` `nexus-search` `infra` `claude` |
| `status:*` | `wip` `blocked` `needs-review` |
| `priority:*` | `high` `low` |

## References
- [git_orchestrator.md](git_orchestrator.md) · `[[git_orchestrator]]`
- [commits.md](commits.md) · `[[commits]]`
- [milestones.md](milestones.md) · `[[milestones]]`
