---
name: languages_orchestrator
description: Routing for per-language feature and idiom references used in Nexus.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - looking up a language feature or idiom
    - routing a language-specific question
metadata:
  type: orchestrator
---

# Languages Orchestrator — Nexus

Per-language **feature & idiom** reference — the language itself, distinct from
GoF patterns ([designpatterns](../designpatterns/design_patterns_orchestrator.md)
· `[[design_patterns_orchestrator]]`) and from project-specific idioms in
[services/nexus-api/idioms](../services/nexus-api/idioms/idioms_orchestrator.md)
· `[[idioms_orchestrator]]`.

Model selection governed by
[model_decision.md](../configurations/model_decision.md) · `[[model_decision]]`.

**New-language rule (mirrors Rule 7):** when a new language enters the project,
add its `languages/<lang>/` reference here alongside its full GoF tree.

---

## Routing Table

| Language | Status | Document |
|---|---|---|
| Kotlin | ✅ active (2.1.21) | [kotlin_language_orchestrator.md](kotlin/kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]` |
| Python | ⏳ v0.6.0 | _added with nexus-ingest_ |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [fundamentals_orchestrator.md](../fundamentals/fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
