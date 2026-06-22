---
name: git-orchestrator
description: Git conventions, branching, versioning, and release practices for Nexus.
agent:
  role: git-workflow-specialist
  tier: standard
  weight: medium
  triggers:
    - starting git work on Nexus
    - routing a git question to the correct doc
metadata:
  type: orchestrator
---

# Git Orchestrator — Nexus

Governs all git conventions for the Nexus repository (one repo, all services).
Any agent performing git work must consult this document first. Model selection
governed by [model_decision.md](../configurations/model_decision.md) · `[[model_decision]]`.

---

## Metadata

| Field | Value |
|---|---|
| Repository | https://github.com/gestreladev/nexus |
| Remote | `git@github-gestreladev:gestreladev/nexus.git` |
| Default branch | `main` (protected) |
| Merge strategy | Squash |
| PR templates | `.github/PULL_REQUEST_TEMPLATE/` (feature, fix, chore, docs) |

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Branching | Branch naming, strategy, protection rules | [branching.md](branching.md) · `[[branching]]` |
| Commits | Conventional Commits format, types, scopes | [commits.md](commits.md) · `[[commits]]` |
| Pull Requests | PR titles, templates, merge strategy, labels | [pull_requests.md](pull_requests.md) · `[[pull_requests]]` |
| Versioning | Semver strategy, release process, changelog, tags | [versioning.md](versioning.md) · `[[versioning]]` |
| Milestones | Semver → milestone mapping, closure criteria | [milestones.md](milestones.md) · `[[milestones]]` |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
