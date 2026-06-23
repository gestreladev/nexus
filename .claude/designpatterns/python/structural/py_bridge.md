---
name: py_bridge
description: Bridge pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Bridge pattern in Python
metadata:
  type: pattern
---

# Bridge

## Intent
Decouple an abstraction from its implementation so both vary independently.

## Structure
```
Abstraction          ---> Implementor (Protocol)
  RefinedAbstraction        ConcreteImplA
                            ConcreteImplB
```

## Python example
```python
from dataclasses import dataclass
from typing import Protocol


class Channel(Protocol):
    def send(self, who: str, text: str) -> None: ...


class Email:
    def send(self, who: str, text: str) -> None:
        print(f"email -> {who}: {text}")


class SMS:
    def send(self, who: str, text: str) -> None:
        print(f"sms -> {who}: {text}")


@dataclass
class Notification:
    channel: Channel  # the implementor, injected

    def notify(self, who: str, body: str) -> None:
        self.channel.send(who, body)


class Alert(Notification):
    def notify(self, who: str, body: str) -> None:
        self.channel.send(who, f"[ALERT] {body}")


Alert(Email()).notify("ana", "disk full")
Notification(SMS()).notify("bob", "build green")
```

## When to use
- Two (or more) dimensions vary independently and you want to avoid a class explosion.
- You need to switch implementations at runtime.
- Abstraction and implementation should be extensible without touching each other.

## Python idiom
Inject the implementor — often just a callable or a `Protocol` — into the abstraction.
No abstract base hierarchy is required: structural typing via `Protocol` keeps the
implementor side open, and a plain function can stand in wherever a one-method
implementor would do.

## In Nexus
Notification type (alert, digest, receipt) and delivery channel (email, SMS, push)
vary on two independent axes; nexus-ingest composes them by injecting a channel
implementor into each notification abstraction rather than subclassing every pair.

## References
- [py_structural_orchestrator.md](py_structural_orchestrator.md) · `[[py_structural_orchestrator]]`
- [py_strategy.md](../behavioral/py_strategy.md) · `[[py_strategy]]`
