---
name: visitor
description: Visitor pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - adding operations over an object structure without changing it
    - separating algorithms from the data they traverse
metadata:
  type: pattern
---

# Visitor

## Intent
Represent an operation to be performed on the elements of an object structure.
Visitor lets you define a new operation without changing the element classes.

## Structure
```
Element.accept(visitor) ──► visitor.visit(thisElement)
ConcreteVisitor implements visit(...) per element type
```

## Kotlin example
```kotlin
sealed interface Node
data class TextNode(val text: String) : Node
data class ImageNode(val url: String) : Node

// "Visitor" via exhaustive when over a sealed hierarchy
fun tokenCount(node: Node): Int = when (node) {
    is TextNode  -> node.text.split(" ").size
    is ImageNode -> 0
}
fun render(node: Node): String = when (node) {     // a second operation, no element change
    is TextNode  -> node.text
    is ImageNode -> "<img src=${node.url}>"
}
```

## When to use
- Many distinct operations over a stable set of element types.
- You'd otherwise scatter type checks across the codebase.

## Kotlin idiom
A `sealed` hierarchy + `when` replaces the classic double-dispatch Visitor: add a
new operation = a new `when` function, no `accept()` boilerplate. Trade-off:
adding a new *element type* forces updating every `when` (the compiler flags them).

## In Nexus
Operations over a document's node tree (token counting, rendering, extraction)
are `when`-based visitors over a sealed `Node` hierarchy.

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [composite.md](../structural/composite.md) · `[[composite]]`
- [interpreter.md](interpreter.md) · `[[interpreter]]`
