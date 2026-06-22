---
name: methodology
description: Pedagogy principles for the Nexus interactive learning engine.
agent:
  role: curriculum-architect
  tier: power
  weight: medium
  triggers:
    - designing a lesson
    - deciding how to teach a concept
    - improving the learning experience
metadata:
  type: guide
---

# Learning Methodology

How we teach Nexus. Calibrated to the learner: strong systems background
(Kotlin/Java/C/C++), Python functional, goal is **system architecture + AI
integration**, ~3h/week. Skip language basics; go deep on architecture.

---

## Principles

1. **Retrieval-first.** Every session opens with a 2-minute active recall of the
   previous lesson before new material. Retrieval beats re-reading.
2. **Exercises before answers.** The learner attempts first; only then is the
   answer reviewed against best practices. Never hand the answer pre-attempt.
3. **Build-to-learn.** Every concept lands as a real Nexus commit with
   acceptance criteria. Theory is grounded in shipped code.
4. **Spaced review.** Each lesson re-touches one concept from ~2 lessons back
   (interleaving) to fight forgetting.
5. **Mastery gates.** A lesson is "done" only when its exercises are correct
   *and* the project task meets acceptance (see [assessment.md](assessment.md) · `[[assessment]]`).
6. **Calibrated depth.** Lean on existing systems knowledge (sockets, memory,
   concurrency) as analogies; spend time on what is genuinely new.
7. **Continuity.** Each lesson ends by writing a `log/` entry capturing answers,
   gaps, and what to revisit — so no thread is ever lost.
8. **Up-to-date, production-grade.** Always current best practices (the project's
   non-negotiable rule). Outdated patterns are flagged and replaced.

---

## Why these (evidence)

- **Active recall + spacing** are the two most robust findings in learning
  science (testing effect, spaced repetition).
- **Worked-example → faded-guidance → independent** matches expertise: this
  learner is past novice, so guidance fades fast.
- **Project-based** retention dwarfs passive reading for engineering skills.

---

## Pacing (3h/week)

A lesson may span one or more weekly sessions. Split a lesson across sessions at
natural seams (concept → exercise in session A; project task → review in B).
The `log/` entry tracks partial progress so resumption is seamless.

## Adaptivity

If an exercise reveals a gap (e.g. a "didn't know" answer), the gap is logged
and re-surfaced in the next session's retrieval step until solid.

## References
- [lesson_orchestrator.md](lesson_orchestrator.md) · `[[lesson_orchestrator]]`
- [lesson_template.md](lesson_template.md) · `[[lesson_template]]`
