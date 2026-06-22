---
name: template_method
description: Template Method pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - fixing an algorithm skeleton with overridable steps
    - sharing a workflow while varying details
metadata:
  type: pattern
---

# Template Method

## Intent
Define the skeleton of an algorithm in a method, deferring some steps to
subclasses. Subclasses redefine steps without changing the structure.

## Structure
```
AbstractClass.templateMethod()  // fixed order, calls:
   ├── step1()  (abstract)
   └── step2()  (abstract / hook)
```

## Kotlin example
```kotlin
abstract class IngestPipeline {
    fun run(raw: String) {            // template method — fixed skeleton
        val parsed = parse(raw)
        val chunks = chunk(parsed)
        store(chunks)
    }
    protected abstract fun parse(raw: String): String
    protected abstract fun chunk(parsed: String): List<String>
    protected open fun store(chunks: List<String>) { /* default */ }
}

class PdfPipeline : IngestPipeline() {
    override fun parse(raw: String) = stripPdf(raw)
    override fun chunk(parsed: String) = parsed.split("\n\n")
}
```

## When to use
- Several variants share a workflow but differ in specific steps.
- You want to enforce the step order centrally.

## Kotlin idiom
Mark the template method non-`open` (final by default) so subclasses can't break
the skeleton; expose `abstract`/`open` step hooks. A higher-order function taking
step lambdas is the functional alternative.

## In Nexus
The ingest workflow (parse→chunk→store) is fixed; per-format pipelines override
only `parse`/`chunk`.

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [strategy.md](strategy.md) · `[[strategy]]`
