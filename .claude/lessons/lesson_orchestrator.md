---
name: lesson_orchestrator
description: Routing for the Nexus interactive learning engine — methodology, format, protocol, logs.
agent:
  role: tutor
  tier: standard
  weight: medium
  triggers:
    - starting or resuming a learning session
    - routing a teaching/pedagogy question
    - looking up where the last lesson stopped
metadata:
  type: orchestrator
---

# Lesson Orchestrator — Nexus

Governs how Nexus is taught: an interactive, build-to-learn class calibrated to
a systems-experienced learner at ~3h/week (see
[user profile in memory] and [methodology.md](methodology.md) · `[[methodology]]`).
Model selection governed by
[model_decision.md](../configurations/model_decision.md) · `[[model_decision]]`.

**Resume rule:** to continue learning, read the latest file in `log/` to see
the last completed lesson and any open gaps, then consult
[progress.md](../project/progress.md) · `[[progress]]` for the next lesson.

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Methodology | Pedagogy principles, how learning works here | [methodology.md](methodology.md) · `[[methodology]]` |
| Lesson template | Canonical structure every lesson follows | [lesson_template.md](lesson_template.md) · `[[lesson_template]]` |
| Session protocol | How a live session opens, runs, and closes | [session_protocol.md](session_protocol.md) · `[[session_protocol]]` |
| Assessment | Exercise grading + mastery checkpoints | [assessment.md](assessment.md) · `[[assessment]]` |
| Glossary | Running glossary of concepts taught | [glossary.md](glossary.md) · `[[glossary]]` |
| Lesson logs | Per-lesson record of topics, answers, gaps | [log/](log/) — `lesson_NN_*.md` |

---

## Lesson index

| # | Phase | Topic | Log | Status |
|---|---|---|---|---|
| 1 | 0 | Foundations: Internet, HTTP, DNS, Git | [lesson_01_foundations.md](log/lesson_01_foundations.md) · `[[lesson_01_foundations]]` | ✅ |
| 2 | 1 | First Service: web servers, REST, OpenAPI | [lesson_02_first_service.md](log/lesson_02_first_service.md) · `[[lesson_02_first_service]]` | ✅ |
| 3 | 2 | Data Layer: PostgreSQL, ACID, migrations, ORM | [lesson_03_database.md](log/lesson_03_database.md) · `[[lesson_03_database]]` | ✅ |
| 4 | 3 | Auth & Security: bcrypt, JWT, OWASP | [lesson_04_auth.md](log/lesson_04_auth.md) · `[[lesson_04_auth]]` | ✅ |
| 5 | 4 | Caching: Redis, cache-aside, JWT denylist | [lesson_05_caching.md](log/lesson_05_caching.md) · `[[lesson_05_caching]]` | ✅ |
| 6 | 5 | Testing & CI/CD: pyramid, fakes, GitHub Actions | [lesson_06_testing.md](log/lesson_06_testing.md) · `[[lesson_06_testing]]` | ✅ |
| 7 | 6 | Python Service: nexus-ingest, FastAPI | _next_ | ⏭️ |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [project_develop_orchestrator.md](../project/project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
