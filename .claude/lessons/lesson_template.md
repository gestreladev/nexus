---
name: lesson_template
description: The canonical structure every Nexus lesson follows.
agent:
  role: curriculum-architect
  tier: standard
  weight: soft
  triggers:
    - authoring a new lesson
    - checking the required lesson sections
metadata:
  type: reference
---

# Lesson Template

Every lesson follows this structure. Sections are mandatory unless marked
optional.

---

## 0. Recap (retrieval) — 2 min
One or two recall questions on the previous lesson + the spaced-review concept
from ~2 lessons back. Learner answers from memory before continuing.

## 1. Concept
The theory and the "why". Use systems analogies. State current best practice
and explicitly flag anything outdated.

## 2. Why it matters in Nexus
Connect the concept to the specific Nexus piece being built this lesson.

## 3. Exercise
Small, isolated, verifiable questions. Learner attempts **before** any answer is
shown. Reviewed against best practices, with refinements.

## 4. Project task
Add the piece to Nexus. Includes explicit **acceptance criteria** and the
target branch/commit. Built to production-grade standards.

## 5. Check
Verify: exercises correct + project task meets acceptance. Run tests / app where
applicable. Only then is the lesson a mastery pass.

## 6. Log + progress update
Write `lessons/log/lesson_NN_*.md` (topics, answers, gaps, status) and update
[progress.md](../project/progress.md) · `[[progress]]` and the relevant
`project/phases/phase_NN.md`.

---

## Roadmap alignment
Each lesson maps to a roadmap.sh/backend phase and a semver milestone. The
mapping lives in [roadmap.md](../project/roadmap.md) · `[[roadmap]]`.

## References
- [methodology.md](methodology.md) · `[[methodology]]`
- [session_protocol.md](session_protocol.md) · `[[session_protocol]]`
- [assessment.md](assessment.md) · `[[assessment]]`
