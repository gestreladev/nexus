---
name: composite
description: Composite pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - modeling part-whole tree structures
    - treating leaves and branches uniformly
metadata:
  type: pattern
---

# Composite

## Intent
Compose objects into tree structures and let clients treat individual objects
and compositions uniformly.

## Structure
```
Component (interface)
   ├── Leaf
   └── Composite ──contains──► many Component
```

## Kotlin example
```kotlin
sealed interface Node { fun size(): Int }

data class FileNode(val bytes: Int) : Node {
    override fun size() = bytes
}
data class FolderNode(val children: List<Node>) : Node {
    override fun size() = children.sumOf { it.size() }   // recurse uniformly
}
```

## When to use
- Data is naturally hierarchical (file systems, document sections, menus).
- Clients should not care whether they hold a leaf or a subtree.

## Kotlin idiom
`sealed interface` + `data class` makes the hierarchy exhaustive and `when`-safe.
Recursion over `children` expresses the whole-part traversal cleanly.

## In Nexus
A document's section tree (headings → subsections → chunks) is a Composite;
`size()`/`tokenCount()` aggregates recursively for chunking.

## References
- [structural_orchestrator.md](structural_orchestrator.md) · `[[structural_orchestrator]]`
