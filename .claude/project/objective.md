---
name: objective
description: The end-state vision and success criteria for the Nexus capstone.
agent:
  role: progress-tracker
  tier: standard
  weight: soft
  triggers:
    - clarifying the project's end goal
    - checking success criteria
metadata:
  type: reference
---

# Objective — Nexus

## Vision
Build **Nexus**, a distributed AI knowledge platform: ingest documents, store
and index them, and answer questions over them with an LLM (RAG). The platform
is the vehicle for learning backend architecture end-to-end and integrating AI.

## Learner goal
System architecture + complete-system vision, aimed at AI integration. Not a job
title — conceptual depth and a real, production-grade system.

## Success criteria (v1.0.0)
- Three services running together: `nexus-api` (Kotlin/Ktor), `nexus-ingest`
  and `nexus-search` (Python/FastAPI).
- PostgreSQL + pgvector, Redis, Kafka, all containerized via Docker Compose.
- Auth, caching, async ingestion, vector search, a working RAG pipeline.
- Observability across services (OpenTelemetry + Grafana).
- Production hardening: load-tested, sharding/replication strategy, graceful
  degradation.
- Every roadmap.sh/backend topic touched through a real deliverable.

## Right-tool-for-the-job
Kotlin for JVM services, Python for AI/data, SQL throughout. New languages get a
focused ramp + their full GoF pattern tree before use.

## Non-negotiable
Everything production-grade and up-to-date with current best practices — the
project's first rule.

## References
- [project_develop_orchestrator.md](project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
- [architecture_vision.md](architecture_vision.md) · `[[architecture_vision]]`
- [roadmap.md](roadmap.md) · `[[roadmap]]`
