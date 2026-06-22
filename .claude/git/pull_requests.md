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
5. **Explain after opening (root Rule 10).** Right after a PR is opened, walk
   through every changed file line by line, explaining each in detail, before
   review/merge. Source of truth is the diff (`gh pr diff <n>`), not memory.

## Post-open walkthrough
The mandatory order once a PR exists:
```
open PR  →  explain all PR files (line by line)  →  review  →  merge
```
No PR skips the walkthrough — it applies to code, config, and `.claude` docs
alike. See [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]` Rule 10.

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
