---
name: phase_11
description: Phase 11 tracker — AI integration (RAG, LLM, agents, MCP).
agent:
  role: progress-tracker
  tier: standard
  weight: soft
  triggers:
    - reviewing or planning Phase 11
    - building the RAG pipeline
metadata:
  type: reference
---

# Phase 11 — AI Integration

| Field | Value |
|---|---|
| Version | `v0.11.0` ⏳ |
| Lesson | 12 |
| Topics | how-llms-work, prompting-techniques, function-calling, structured-outputs, rags, agents, anthropic, mcp |
| Status | ⏳ planned |

## Planned deliverables
- [ ] `nexus-search` FastAPI service
- [ ] RAG pipeline: retrieve (pgvector) → augment → generate (LLM)
- [ ] Streaming responses; structured outputs; tool/function calling
- [ ] LLM client behind a rate-limiting proxy (Proxy pattern)

## Note
This is the AI-integration payoff — the learner's primary goal. Use latest
Claude models and current RAG best practices.

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [objective.md](../objective.md) · `[[objective]]`
