#!/usr/bin/env python3
"""Structural verification of the .claude vault. Deterministic, no network."""
import os
import re
import sys

ROOT = os.path.dirname(os.path.abspath(__file__))
MD = []
for dp, _, fns in os.walk(ROOT):
    for fn in fns:
        if fn.endswith(".md"):
            MD.append(os.path.join(dp, fn))

errors = []
basenames = {}
for p in MD:
    stem = os.path.basename(p)[:-3]
    basenames.setdefault(stem, []).append(p)

# 1. duplicate basenames (wikilinks must resolve uniquely)
for stem, paths in basenames.items():
    if len(paths) > 1:
        errors.append(f"DUP basename '{stem}': {[os.path.relpath(x, ROOT) for x in paths]}")

LINK = re.compile(r"\]\(([^)]+)\)")
WIKI = re.compile(r"\[\[([^\]]+)\]\]")
FM_KEYS = ("role:", "tier:", "weight:")

def strip_code(text):
    """Remove fenced blocks and inline code so examples aren't parsed as links."""
    out, in_fence = [], False
    for ln in text.splitlines():
        if ln.lstrip().startswith("```"):
            in_fence = not in_fence
            continue
        if in_fence:
            continue
        out.append(re.sub(r"`[^`]*`", "", ln))  # drop inline code spans
    return "\n".join(out)


for p in MD:
    rel = os.path.relpath(p, ROOT)
    with open(p, encoding="utf-8") as f:
        text = f.read()
    lines = text.splitlines()
    scan = strip_code(text)  # links/wikilinks checked against code-free view

    # 2. line limit
    if len(lines) > 200:
        errors.append(f"LINELIMIT {rel}: {len(lines)} > 200")

    # 3. frontmatter agent block
    if not text.startswith("---"):
        errors.append(f"FRONTMATTER {rel}: missing opening ---")
    else:
        head = text.split("---", 2)[1] if text.count("---") >= 2 else ""
        for k in FM_KEYS:
            if k not in head:
                errors.append(f"FRONTMATTER {rel}: missing '{k}'")

    # 4. relative link integrity
    for m in LINK.finditer(scan):
        target = m.group(1).split("#")[0].strip()
        if not target or target.startswith(("http://", "https://", "mailto:")):
            continue
        resolved = os.path.normpath(os.path.join(os.path.dirname(p), target))
        if not os.path.exists(resolved):
            errors.append(f"DEADLINK {rel}: '{target}'")

    # 5. wikilink resolution (basename must exist as a .md stem)
    for m in WIKI.finditer(scan):
        name = m.group(1).split("|")[0].split("#")[0].strip()
        if name not in basenames:
            errors.append(f"DEADWIKI {rel}: [[{name}]]")

print(f"Scanned {len(MD)} markdown files.")
if errors:
    print(f"\n{len(errors)} ISSUE(S):")
    for e in sorted(errors):
        print("  -", e)
    sys.exit(1)
print("✅ Vault clean: line limits, links, wikilinks, frontmatter, unique basenames.")
