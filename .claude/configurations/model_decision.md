---
name: model-decision
description: Tier reference and task-to-tier mapping for model selection in Nexus.
agent:
  role: model-selector
  tier: standard
  weight: soft
  triggers:
    - selecting a model tier before starting a task
    - justifying Power usage
    - classifying task weight and spawn limits
metadata:
  type: config
---

# Model Decision Rules

Defines which model tier to use by task type, and the task-weight spawn limits.
Any agent starting a task must map it to a tier before proceeding.

---

## Tier Reference

| Tier | Model ID | Use when |
|---|---|---|
| Nano | `claude-haiku-4-5-20251001` | Single-file edits, renames, line deletions, CLI commands, yes/no decisions |
| Standard | `claude-sonnet-4-6` | Cross-file reasoning, refactors, structured generation, summaries, most implementation |
| Power | `claude-opus-4-8` | Deep architecture decisions, ambiguous multi-constraint problems, high-stakes design, lesson/roadmap planning |

---

## Task-to-Tier Mapping

| Task type | Tier |
|---|---|
| Fix a typo or rename | Nano |
| Delete a file or line | Nano |
| Run a CLI / git command | Nano |
| Single-file formatting or import fix | Nano |
| Write a route, repository, or test | Standard |
| Migrate / refactor across files | Standard |
| Generate a doc or pattern leaf | Standard |
| Cross-file consistency check | Standard |
| Architecture or layer design | Power |
| Lesson / roadmap planning | Power |
| Ambiguous multi-constraint problem | Power |

---

## Task Weight + Spawn Limits

| Weight | Scope | Max agents | Tiers permitted |
|---|---|---|---|
| Soft | 1–2 files, single concern | 2 | Standard, Nano (Power banned) |
| Medium | 3–8 files, cross-cutting | 6 | Standard, Nano; Power only on unplanned architecture |
| Heavy | 9+ files or feature-level | 16 | Standard, Nano; Power per-spawn justified |

---

## Enforcement

1. **Declare before starting** — state the tier and (if spawning) the task
   weight + one-sentence justification.
2. **Never pre-escalate** — do not assume Power without justification.
3. **Exceeding the spawn cap is a violation** — split or re-classify instead.
4. **Dispatch (recommended)** — for multi-step or specialist work, dispatch via
   the `Agent`/`Workflow` tool with an explicit `model` matching the tier.

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [agent_roles.md](agent_roles.md) · `[[agent_roles]]`
