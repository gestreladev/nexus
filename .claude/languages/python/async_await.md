---
name: async_await
description: Python async/await and asyncio — coroutines, concurrency, async with.
agent:
  role: language-expert
  tier: standard
  weight: medium
  triggers:
    - writing asynchronous code
    - concurrency in FastAPI / ingest
metadata:
  type: reference
---

# Async / await

`async def` defines a **coroutine**; `await` suspends it without blocking the
thread — so one process handles many concurrent I/O-bound requests. FastAPI is
async-first. This is Python's analogue to Kotlin coroutines.

```python
import asyncio

async def fetch_user(uid: str) -> User: ...

async def main() -> None:
    print("Hello"); await asyncio.sleep(1); print("World")

asyncio.run(main())          # the entry point (manages the event loop)
```

## Concurrency — gather
Run awaitables concurrently and collect results:
```python
async def dashboard() -> Dashboard:
    docs, stats = await asyncio.gather(recent_docs(), compute_stats())
    return Dashboard(docs, stats)
```
- `gather` — run together, wait for all (like Kotlin `async`/`await`).
- `asyncio.TaskGroup` (3.11+) — structured concurrency: cancels siblings on error.
- `async with` / `async for` — async context managers and iterators.

## The cardinal rule
**Never call a blocking function inside a coroutine** — it stalls the whole event
loop. Use async libraries (async DB/HTTP clients) or offload blocking work to a
thread (`asyncio.to_thread(...)`).

## In Nexus
`nexus-ingest`/`nexus-search` handlers are `async`; LLM and DB calls use async
clients so one worker serves many in-flight requests. Heavy CPU work (embedding)
is offloaded so it doesn't block the loop.

## References
- [python_language_orchestrator.md](python_language_orchestrator.md) · `[[python_language_orchestrator]]`
- [coroutines.md](../kotlin/coroutines.md) · `[[coroutines]]` — the Kotlin analogue
