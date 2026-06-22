---
name: project_develop_orchestrator
description: Routing for Nexus development tracking — objective, roadmap, progress, phases.
agent:
  role: progress-tracker
  tier: standard
  weight: medium
  triggers:
    - checking project progress or what's next
    - planning a phase
    - recording a development decision
metadata:
  type: orchestrator
---

# Project Develop Orchestrator — Nexus

Single source of truth for **what we are building and how far we've come**. The
objective is the Nexus capstone; this domain tracks progress toward it across 12
roadmap phases. Model selection governed by
[model_decision.md](../configurations/model_decision.md) · `[[model_decision]]`.

**Resume rule:** read [progress.md](progress.md) · `[[progress]]` for current
state and next action.

---

## Metadata

| Field | Value |
|---|---|
| Objective | Distributed AI knowledge platform (see [objective.md](objective.md)) |
| Current version | `v0.2.0` released |
| Current phase | Phase 2 done → **Phase 3 (Auth & Security) next** |
| Versions/milestones | tracked in [git/milestones.md](../git/milestones.md) · `[[milestones]]` |

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Objective | The end-state vision and success criteria | [objective.md](objective.md) · `[[objective]]` |
| Roadmap | 12 phases × roadmap.sh topics × versions × lessons | [roadmap.md](roadmap.md) · `[[roadmap]]` |
| Progress | Live status + next action (the resume pointer) | [progress.md](progress.md) · `[[progress]]` |
| Architecture vision | Target Nexus system diagram | [architecture_vision.md](architecture_vision.md) · `[[architecture_vision]]` |
| Decisions | ADR-style log of key choices | [decisions.md](decisions.md) · `[[decisions]]` |
| Phase trackers | Per-phase deliverables + status | [phases/](phases/) — `phase_NN.md` |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [lesson_orchestrator.md](../lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]`
