---
name: claude-usage
description: How to navigate and maintain the Nexus .claude knowledge base.
agent:
  role: reference-reader
  tier: nano
  weight: soft
  triggers:
    - learning how the .claude structure works
    - checking the link or disclosure conventions
    - adding a new doc to the knowledge base
metadata:
  type: guide
---

# .claude Usage Guide

How to interact with the Nexus knowledge base — navigation, conventions, and
maintenance. Mirrors the vault-omniverse organizational pattern.

---

## Navigation

Start at [CLAUDE.md](../CLAUDE.md) (root). Follow its Routing Table to a domain
or service orchestrator, then to a leaf. Load only what the task needs (Rule 1).

```
CLAUDE.md → <domain>_orchestrator.md → [sub_orchestrator.md] → leaf.md
```

---

## Link convention (dual)

Every routing-table Document cell carries **both** forms:

```
[layers.md](services/nexus-api/architecture/layers.md) · `[[layers]]`
```

- **Relative markdown link** — clickable on GitHub and in IDEs.
- **`[[name]]` wikilink** — Obsidian / graph-view parity; the slug matches the
  target **filename** without `.md` (e.g. `[[layers]]` → `layers.md`), which is
  how Obsidian resolves links.

---

## Disclosure convention (Rule 3)

Every response opens with a context line, grouped by domain emoji:

```
📂 Context loaded: 🌿 git/branching.md · 🏛️ services/nexus-api/architecture/layers.md
```

| Domain | Emoji |
|---|---|
| Git | 🌿 |
| Architecture | 🏛️ |
| API | 🔌 |
| Idioms | ✍️ |
| Testing | 🧪 |
| Design Patterns | 🧩 |
| Config / Vault | 🗂️ |
| Service | 📁 |

---

## Adding a new doc

1. Place it under the correct domain/service folder.
2. Add the full `agent:` frontmatter (Rule 6) — role from
   [agent_roles.md](agent_roles.md) · `[[agent_roles]]`.
3. Add a row to the parent orchestrator's Routing Table (dual link).
4. Keep it ≤ 200 lines (Rule 2).
5. Add a `## References` section linking the parent orchestrator.

---

## Tooling

| Task | Tool |
|---|---|
| Read / search docs | Read tool |
| Create / edit docs | Write / Edit tool |
| Commit + push | git CLI |
| Open PR | `gh` CLI (uses `.github/PULL_REQUEST_TEMPLATE/`) |

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
