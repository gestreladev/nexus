---
name: py_mediator
description: Mediator pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Mediator pattern in Python
metadata:
  type: pattern
---

# Mediator

## Intent
Centralize many-to-many communication between objects. Instead of components referencing each other directly, they talk through a single mediator, which keeps coupling low and interaction logic in one place.

## Structure

```
ComponentA --\            /--> ComponentB
              >-- Mediator --
ComponentC --/            \--> ComponentD

   (peers notify the mediator; it routes to the right handlers)
```

## Python example

```python
from collections import defaultdict
from typing import Callable

Handler = Callable[[dict], None]


class EventHub:
    def __init__(self) -> None:
        self._subs: dict[str, list[Handler]] = defaultdict(list)

    def on(self, event: str) -> Callable[[Handler], Handler]:
        def register(fn: Handler) -> Handler:
            self._subs[event].append(fn)
            return fn
        return register

    def emit(self, event: str, payload: dict) -> None:
        for handler in self._subs[event]:
            handler(payload)


hub = EventHub()


@hub.on("doc_parsed")
def index_doc(payload: dict) -> None:
    print(f"indexing {payload['id']}")


hub.emit("doc_parsed", {"id": 42})  # -> indexing 42
```

## When to use
- Many components interact in a tangled mesh and direct references are hard to maintain.
- You want to vary or extend interaction logic without touching the peers.
- Cross-cutting coordination (logging, sequencing, validation) belongs in one place.

## Python idiom
The idiomatic shortcut is an event-hub object dispatching to registered callbacks. Rather than a heavyweight mediator interface per peer, you keep a `dict[str, list[Callable]]` and let components register handlers (often via a decorator like `@hub.on(...)`). Emitting an event fans out to all subscribers, so peers never import one another.

## In Nexus
An in-process `EventHub` coordinates ingest stages (parse, enrich, index) so `nexus-ingest` and `nexus-search` stay decoupled before any of it moves onto Kafka. The hub is the seam: today it dispatches in-process, tomorrow the same `emit` calls publish to a topic.

## References
- [py_behavioral_orchestrator.md](py_behavioral_orchestrator.md) · `[[py_behavioral_orchestrator]]`
- [py_observer.md](py_observer.md) · `[[py_observer]]`
