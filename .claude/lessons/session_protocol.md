---
name: session_protocol
description: How a live Nexus learning session opens, runs, and closes.
agent:
  role: tutor
  tier: standard
  weight: soft
  triggers:
    - starting a learning session
    - resuming after a break or context compaction
metadata:
  type: guide
---

# Session Protocol

The operational checklist for running a live session. Complements
[lesson_template.md](lesson_template.md) · `[[lesson_template]]` (content) with
*how the session is conducted*.

---

## Open
1. Emit the `📂 Context loaded:` disclosure (root Rule 3).
2. **Resume check:** read the latest `log/` entry + [progress.md](../project/progress.md)
   · `[[progress]]` to confirm the current lesson and open gaps.
3. Run the **Recap** (retrieval) step — prior lesson + the spaced-review concept.
4. State the lesson goal and which roadmap topics/milestone it covers.

## Run
5. Teach Concept → Why-in-Nexus.
6. Pose the Exercise. **Wait** for the learner's attempt before reviewing.
7. Review answers; refine; surface best-practice nuances.
8. Move to the Project task with explicit acceptance criteria.
9. Build on a `feat/*` branch; verify (tests/app); open PR.

## Close
10. Confirm mastery (exercises correct + acceptance met).
11. Write/append the `log/` entry (answers, gaps, status, partial-progress note
    if the lesson spans sessions).
12. Update [progress.md](../project/progress.md) · `[[progress]]` and the phase
    tracker; add new terms to [glossary.md](glossary.md) · `[[glossary]]`.
13. State the next lesson and any gaps queued for the next Recap.

---

## Spanning multiple weekly sessions
A lesson may pause at a natural seam. The `log/` entry records the seam and what
remains, so the next session resumes precisely.

## Resuming after context compaction
Never rely on conversation memory alone — re-read the latest `log/` entry and
`progress.md`. The vault is the source of truth.

## References
- [lesson_orchestrator.md](lesson_orchestrator.md) · `[[lesson_orchestrator]]`
- [methodology.md](methodology.md) · `[[methodology]]`
