---
name: command
description: Command pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - encapsulating an action as an object
    - queuing, logging, or undoing operations
metadata:
  type: pattern
---

# Command

## Intent
Encapsulate a request as an object, letting you parameterize, queue, log, and
undo operations.

## Structure
```
Invoker ──► Command.execute() ──► Receiver (does the work)
```

## Kotlin example
```kotlin
fun interface Command { fun execute() }      // SAM — a lambda is a Command

class CommandBus {
    private val queue = ArrayDeque<Command>()
    fun submit(c: Command) { queue.addLast(c) }
    fun drain() { while (queue.isNotEmpty()) queue.removeFirst().execute() }
}

val bus = CommandBus()
bus.submit { repository.reindex(docId) }      // lambda as Command
bus.drain()
```

## When to use
- Decouple the requester from the executor.
- Queue, schedule, retry, log, or undo operations.

## Kotlin idiom
`fun interface` (SAM) lets any lambda be a Command — no concrete class per
action. For undo, model commands as a `sealed interface` with `execute()`/`undo()`.

## In Nexus
Async ingestion jobs are commands placed on a queue/bus (Kafka in v0.7.0),
executed by consumers — decoupling submission from processing.

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
