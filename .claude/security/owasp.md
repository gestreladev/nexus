---
name: owasp
description: OWASP Top 10 risks and Nexus mitigations.
agent:
  role: security-auditor
  tier: standard
  weight: medium
  triggers:
    - threat-modeling an endpoint
    - reviewing for common web vulnerabilities
metadata:
  type: reference
---

# OWASP Top 10 — Nexus

The OWASP Top 10 is the industry baseline of web risks. Review any new endpoint
against it. Selected items + how Nexus addresses them:

| Risk | Nexus mitigation |
|---|---|
| **Broken Access Control** | per-request JWT auth; ownership checks (403) for user-scoped resources (rolling in) |
| **Cryptographic Failures** | bcrypt for passwords; TLS in transit; no secrets in logs/tokens |
| **Injection** | Exposed DSL parameterizes all SQL — no string-built queries |
| **Insecure Design** | stateless auth, generic 401 (anti-enumeration), least privilege |
| **Security Misconfiguration** | secrets via env; `Cache-Control: no-store` on auth; minimal images |
| **Vulnerable Components** | pinned deps; version-bump PRs; image scanning in CI (Phase 5) |
| **Auth Failures** | bcrypt + short-lived JWT + denylist revocation; rate-limit login (planned) |
| **Integrity Failures** | signed JWT (tamper-evident); dependency integrity |
| **Logging/Monitoring** | structured logs + OpenTelemetry (Phase 10) |
| **SSRF** | validate/allow-list any outbound URLs (relevant at ingest/LLM phases) |

## Practice
- Treat every input as hostile; validate at the boundary (400 on bad input).
- Never leak internals in errors (the single `ErrorResponse` shape).
- Default deny: protected by default, public by explicit choice.

## References
- [security_orchestrator.md](security_orchestrator.md) · `[[security_orchestrator]]`
- [conventions.md](../services/nexus-api/api/conventions.md) · `[[conventions]]`
- [OWASP Top 10](https://owasp.org/Top10/)
