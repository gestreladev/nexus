---
name: state
description: State pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - changing behavior based on internal state
    - modeling a state machine
metadata:
  type: pattern
---

# State

## Intent
Allow an object to alter its behavior when its internal state changes — the
object appears to change its class.

## Structure
```
Context.state: State
Context delegates behavior to current State, which may transition to another
```

## Kotlin example
```kotlin
sealed interface DocState {
    fun process(): DocState
}
data object Pending : DocState { override fun process() = Processing }
data object Processing : DocState { override fun process() = Ready }
data object Ready : DocState { override fun process() = this }      // terminal
data object Failed : DocState { override fun process() = this }

class Document(var state: DocState = Pending) {
    fun advance() { state = state.process() }
}
```

## When to use
- Behavior depends on state and there are many state-specific branches.
- Replace sprawling `if/when (status)` chains with explicit state objects.

## Kotlin idiom
`sealed interface` + `data object` states; transitions return the next state.
`when` over the sealed type is exhaustive — the compiler flags a missing state.

## In Nexus
The `documents.status` lifecycle (pending→processing→ready|failed) is a State
machine; transitions live on the state types, not scattered across services.

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [strategy.md](strategy.md) · `[[strategy]]`
