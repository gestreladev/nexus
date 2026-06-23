---
name: py_adapter
description: Adapter pattern in idiomatic Python.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - applying the Adapter pattern in Python
metadata:
  type: pattern
---

# Adapter

## Intent
Convert one interface into another clients expect, letting otherwise incompatible classes work together without changing their source.

## Structure

```
Client --> Target (expected interface)
              ^
              |
           Adapter ----> Adaptee (incompatible interface)
```

## Python example

```python
from dataclasses import dataclass
from typing import Protocol


class VectorStore(Protocol):  # Target port the domain expects
    def search(self, query: str, k: int) -> list[str]: ...


class VendorClient:  # Adaptee with an incompatible interface
    def query(self, text: str, top_n: int) -> dict[str, list[str]]:
        return {"hits": [f"{text}-{i}" for i in range(top_n)]}


@dataclass
class VendorVectorStore:  # Adapter: VendorClient -> VectorStore
    client: VendorClient

    def search(self, query: str, k: int) -> list[str]:
        return self.client.query(query, top_n=k)["hits"]


def run(store: VectorStore) -> list[str]:
    return store.search("nexus", k=3)


print(run(VendorVectorStore(VendorClient())))
```

## When to use

- A third-party or legacy class exposes an interface your client cannot use directly.
- You want to keep domain code decoupled from a vendor's naming and call shape.
- You need several interchangeable backends behind one stable port.

## Python idiom
Often a thin wrapper class (as above) or even a single adapting function is enough; you rarely need a formal interface hierarchy. Because of duck typing and `typing.Protocol`, an object that already has the right method shape needs no adapter at all, so reach for one only when the shapes genuinely differ.

## In Nexus
Wrap a vendor vector-store client behind a domain `VectorStore` port so `nexus-ingest` and `nexus-search` stay vendor-free and swappable. The domain depends on the port, never on the SDK.

## References
- [py_structural_orchestrator.md](py_structural_orchestrator.md) · `[[py_structural_orchestrator]]`
- [py_facade.md](py_facade.md) · `[[py_facade]]`
