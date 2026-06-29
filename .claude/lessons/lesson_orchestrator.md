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

## Mandatory Rule — Faithful walkthrough abbreviations
When explaining code in a lesson or PR walkthrough (root Rule 10), abbreviated
snippets must stay **self-consistent**:
- **Never elide the line that defines** a variable/symbol the snippet then
  references — an undefined-looking reference makes the code read as broken.
- Mark every omission explicitly with `...` / `…`; what's shown must hold
  together on its own.
- **When in doubt about the real code, read the file** — don't reconstruct a
  snippet from memory.

A walkthrough's job is to clarify, not to make correct code look wrong. For the
**presentation** of a PR walkthrough (the required visual widget), see
[pr_walkthrough.md](../git/pr_walkthrough.md) · `[[pr_walkthrough]]`.

---

## Mandatory Rule — pt-BR translation on difficulty
When the learner signals difficulty with a topic (says they didn't understand,
struggled, or "had a hard time"), **re-explain that topic in Brazilian
Portuguese (pt-BR)** — not just the English again:
- Lead with a concise pt-BR explanation grounded in a concrete analogy; keep
  Nexus/code identifiers (`kafka:29092`, `${VAR:?}`) and accepted technical
  terms in their original form.
- Then give a one-line English↔pt-BR term bridge for the key vocabulary, so the
  English names still stick.
- Log the topic as a gap in the lesson `log/` and re-surface it (in pt-BR if
  still shaky) at the next retrieval step until solid.

The goal is comprehension first; the learner is fluent in pt-BR, so a second
language channel removes the bottleneck without dumbing the content down.

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
| 7 | 6 | Python Service: nexus-ingest, FastAPI | [lesson_07_python_service.md](log/lesson_07_python_service.md) · `[[lesson_07_python_service]]` | ✅ |
| 8 | 7 | Async & Messaging: Kafka producer → consumer | [lesson_08_messaging.md](log/lesson_08_messaging.md) · `[[lesson_08_messaging]]` | ✅ |
| 9 | 8 | Containers & Architecture: Docker Compose, microservices | [lesson_09_containers.md](log/lesson_09_containers.md) · `[[lesson_09_containers]]` | ✅ |
| 10 | 9 | Search & Vectors: embeddings, pgvector, Strategy | [lesson_10_search.md](log/lesson_10_search.md) · `[[lesson_10_search]]` | ✅ |
| 11 | 10 | Observability: OpenTelemetry, tracing, RED, logs | [lesson_11_observability.md](log/lesson_11_observability.md) · `[[lesson_11_observability]]` | 🔄 |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [project_develop_orchestrator.md](../project/project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
