---
name: assessment
description: Exercise grading and mastery checkpoints for Nexus lessons.
agent:
  role: assessor
  tier: standard
  weight: soft
  triggers:
    - grading a lesson exercise
    - deciding whether a lesson is mastered
metadata:
  type: reference
---

# Assessment

How understanding is verified. A lesson is not "done" until it passes both
gates below.

---

## Gate 1 — Exercises
Each exercise answer is graded against best practice:

| Verdict | Meaning | Action |
|---|---|---|
| ✅ Correct | Sound and complete | Note any deeper nuance, move on |
| 🟡 Partial | Right direction, missing depth | Refine with the learner, re-state the rule |
| ❌ Gap | Incorrect or "didn't know" | Explain fully; log as a gap to re-surface |

Partial/Gap items are recorded in the lesson `log/` and re-asked in the next
session's Recap until ✅.

## Gate 2 — Project task acceptance
The lesson's Nexus deliverable must meet its explicit acceptance criteria:
- Compiles / tests pass / app runs (verified, not assumed)
- Production-grade: typed, error-handled, no hardcoded secrets, versioned API
- Merged via PR with milestone + label

Only when **both** gates pass is the lesson a **mastery pass** and the phase
tracker updated.

---

## Mastery signals over time
- A concept is "solid" once it survives a spaced-review Recap without a gap.
- Recurring gaps in a topic trigger a dedicated mini-review before advancing.

## Self-assessment prompt (optional, end of phase)
At each version milestone, a short retrieval quiz across the phase's concepts
confirms durable retention before moving to the next phase.

## References
- [methodology.md](methodology.md) · `[[methodology]]`
- [lesson_template.md](lesson_template.md) · `[[lesson_template]]`
