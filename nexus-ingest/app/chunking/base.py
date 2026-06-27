"""The chunking Strategy contract."""

from typing import Protocol, runtime_checkable


@runtime_checkable
class ChunkingStrategy(Protocol):
    """Split a document's text into embeddable chunks."""

    def chunk(self, text: str) -> list[str]:
        ...
