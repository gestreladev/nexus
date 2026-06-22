---
name: memento
description: Memento pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - capturing and restoring an object's state
    - implementing undo or snapshots
metadata:
  type: pattern
---

# Memento

## Intent
Capture an object's internal state without violating encapsulation, so it can be
restored later.

## Structure
```
Originator.save() ──► Memento ──► Caretaker stores it
Originator.restore(memento) ──► state rolled back
```

## Kotlin example
```kotlin
class Editor(var content: String = "") {
    fun save(): Snapshot = Snapshot(content)         // memento
    fun restore(s: Snapshot) { content = s.content }
    data class Snapshot(val content: String)         // immutable, opaque to others
}

val editor = Editor("draft")
val checkpoint = editor.save()
editor.content = "edited"
editor.restore(checkpoint)        // back to "draft"
```

## When to use
- Undo/redo, transactional rollback, snapshots/checkpoints.

## Kotlin idiom
An immutable `data class` as the memento; only the originator can produce/consume
it. Because `data class` is immutable, the snapshot can't be mutated after capture.

## In Nexus
Document edit history / draft checkpoints can snapshot state via Memento before
re-ingestion, enabling rollback on failure.

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [prototype.md](../creational/prototype.md) · `[[prototype]]`
