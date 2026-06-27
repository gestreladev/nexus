---
name: pr_walkthrough
description: Required visual presentation for the Rule 10 post-open PR walkthrough.
agent:
  role: git-workflow-specialist
  tier: standard
  weight: medium
  triggers:
    - presenting a PR walkthrough after opening a PR
    - deciding how to show "every changed file" (Rule 10)
metadata:
  type: guide
---

# PR Walkthrough — Presentation Standard

Rule 10 says *walk through every file in a PR*. **How** it is presented is fixed
here: a single **visual widget** (the `show_widget` / visualize tool), not a wall
of text. The widget makes 50+ files scannable; the deep line-by-line reading goes
into response text or behind drill-in buttons.

Skeleton to copy: [pr_walkthrough_widget.html](templates/pr_walkthrough_widget.html).

## Two standing mandates
- **Always in pt-BR.** Every widget caption, section label, file one-liner, and
  "why" line is Brazilian Portuguese (the learner's language — see
  [lesson_orchestrator.md](../lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]`).
  Code, identifiers, diff contents, and accepted technical terms (`HNSW`,
  `ON CONFLICT`, `Strategy`) stay as-is — code is code.
- **Always show real diffs.** The widget MUST include the actual changed lines of
  the headline files as **diff hunks** (taken from `gh pr diff <n>`), not just
  prose descriptions. A walkthrough with no code diff is incomplete. At least the
  key change is shown as a diff; see §Code visuals for the budget.

---

## Required sections (in order)
1. **Header** — `PR #<n>` chip · title · milestone pill, then 3–4 metric cards
   (files, `+adds / −dels`, tests, group count). Numbers come from
   `gh pr view <n> --json additions,deletions,changedFiles,files`.
2. **Orientation strip** — the data-flow / architecture the PR builds (≤5 nodes,
   Tabler icons). Omit only when the PR has no meaningful flow (pure docs).
3. **Key-change highlight** — one callout for the single most important or subtle
   change; use a before→after pair when it clarifies (e.g. `3 → 30 plugins`), and
   **always back it with a diff hunk** of the real changed lines (§Code visuals).
4. **Verification / proof** — the evidence it works as compact rows (test counts,
   live results, query→result). Not a `<table>` — flex rows.
5. **Complete file map** — **every** changed file, grouped (A, B, C…). Each group
   is a collapsed `<details>` with rows of `code` filename + one-line intent.
   Group A open by default; an "expand all" toggle reveals the rest.
6. **Drill-in buttons** — 1–3 `sendPrompt('Walk me through <file>…')` for the
   files most worth a line-by-line read.

---

## Faithfulness (non-negotiable)
- **Every changed file appears.** The group counts must sum to the PR's file
  count — the widget is not a summary, it is full coverage (Rule 10).
- Each one-liner is **accurate to the diff** (`gh pr diff <n>`), never invented.
- Keep [the abbreviation rule](../lessons/lesson_orchestrator.md) · `[[lesson_orchestrator]]`:
  a row that names a symbol must not contradict the code.
- The widget presents; **depth still happens** — either in response prose for the
  headline files or via the drill-in buttons. Don't let the visual replace the
  explanation, only organize it.

## Design rules (visualize system)
- CSS variables only (dark-mode safe); Tabler **outline** icons; sentence case;
  no emoji; weights 400/500 only.
- Collapse logic runs **after** streaming (script at the end); groups stream open
  so no content is hidden mid-stream, then JS collapses all but group A.
- One widget per PR. Response text carries the merge/next-step prompt.

## Code visuals
A code visual earns a slot only when *what changed* can't be trusted from prose —
the reviewer would otherwise open the file to see the exact line. Three forms, one
vocabulary (classes live in the skeleton).

### Which form
- **Diff hunk** (`.hunk-card` + `.dh-*`) — **required**, the primary visual; at
  least one per walkthrough (the key change). For "does this line do the right
  thing?": a flipped boolean, new guard, off-by-one, signature change,
  concat→parameterized swap. Removed/added lines adjacent; hand-authored unified
  diff in one `<pre>`. New files show their key lines as `+` additions.
- **Annotation** (`.an-*` + `.fam-*`) — secondary, only when **1–3** lines carry
  the correctness argument (invariant, lock, atomic op, tenant `WHERE`). Each note
  says **why** the line matters, not what it does. Never pin the signature line.
- **Diagram beats code** for *structural* change (a new call edge, a reordered
  sequence) — use the orientation strip, not a hunk. Never both for one change.

A syntax-only snippet (no diff, no pins) rarely earns a headline slot — reuse the
`.tk-*` token spans inside a hunk instead.

### Budget (anti-code-dump)
- **1–4 headline hunks** (≥1 required, hard cap 4), each **≤12 diff lines**
  (~8 ideal). Past 4 it reads as a diff viewer, not a walkthrough.
- A hunk anchors to the key-change bullet it proves: claim → hunk → proof → file
  map. ≤2 hunks before the proof section, the rest after.
- Everything past the budget moves **behind the file-map drill-in**
  (`sendPrompt('Walk me through <file>')`) — reached by intent, not first scan. A
  5th essential hunk means group it under the map, don't raise the cap.

### Constraints (streaming + dark mode)
- **Hand-authored markup only — no JS highlighter.** Diff rows, gutter signs, pins,
  and `.tk-*` tokens are literal markup, so code is styled from the first token;
  highlight.js/Prism run only *after* streaming and flash unstyled.
- **CSS variables only**: add/del = `--color-background-success`/`-danger`, text in
  the matching family (darkest shade, never gray); `tk-k`→`text-info`,
  `tk-c`→`text-tertiary`.
- One `<pre>`, `overflow-x:auto`, **no nested vertical scroll**. Single-sided
  `border-left` accents → `border-radius:0`. Escape `<` and `&`; the delete gutter
  and `−N` stat use U+2212 (`&#8722;`), not a hyphen.

## Workflow placement
```
open PR  →  Rule 10 walkthrough WIDGET  →  review  →  squash merge
```
See [pull_requests.md](pull_requests.md) · `[[pull_requests]]` and
[CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]` Rule 10.

## References
- [pr_walkthrough_widget.html](templates/pr_walkthrough_widget.html) — the skeleton
- [git_orchestrator.md](git_orchestrator.md) · `[[git_orchestrator]]`
- [pull_requests.md](pull_requests.md) · `[[pull_requests]]`
