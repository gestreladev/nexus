---
name: interpreter
description: Interpreter pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - evaluating expressions in a small language
    - building a query or filter grammar
metadata:
  type: pattern
---

# Interpreter

## Intent
Given a language, define a representation of its grammar and an interpreter that
uses the representation to evaluate sentences.

## Structure
```
Expression.interpret(context)
   ├── TerminalExpression
   └── NonterminalExpression ──► composes sub-expressions
```

## Kotlin example
```kotlin
sealed interface Expr { fun eval(facts: Set<String>): Boolean }

data class Has(val tag: String) : Expr {
    override fun eval(facts: Set<String>) = tag in facts
}
data class And(val l: Expr, val r: Expr) : Expr {
    override fun eval(facts: Set<String>) = l.eval(facts) && r.eval(facts)
}
data class Or(val l: Expr, val r: Expr) : Expr {
    override fun eval(facts: Set<String>) = l.eval(facts) || r.eval(facts)
}

// (has "kotlin") AND (has "backend")
val rule = And(Has("kotlin"), Has("backend"))
rule.eval(setOf("kotlin", "backend"))   // true
```

## When to use
- A simple, stable domain language (filters, rules, search syntax).
- Reach for a parser generator instead when the grammar is large/complex.

## Kotlin idiom
`sealed interface` expression trees with an `eval` method; `when` over the
sealed type is exhaustive. Pairs naturally with Composite and Visitor.

## In Nexus
A document-filter mini-language (`status:ready AND tag:kotlin`) for search
queries can be modeled as an Interpreter over a sealed `Expr` tree.

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [visitor.md](visitor.md) · `[[visitor]]`
