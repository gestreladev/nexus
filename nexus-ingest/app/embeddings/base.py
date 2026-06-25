"""The embedding Strategy contract.

Two methods, not one, because retrieval is *asymmetric*: documents and queries
are embedded with different instructions/prompts (Voyage `input_type`, the local
model's query prompt) so a query vector lands near the passages that answer it.
"""

from typing import Protocol, runtime_checkable


@runtime_checkable
class EmbeddingStrategy(Protocol):
    """A swappable text→vector implementation (GoF Strategy)."""

    @property
    def dimension(self) -> int:
        """Output vector length; must match the `document_chunks.embedding` column."""
        ...

    def embed_documents(self, texts: list[str]) -> list[list[float]]:
        """Embed stored passages (write path)."""
        ...

    def embed_query(self, text: str) -> list[float]:
        """Embed a search query (read path) — asymmetric to documents."""
        ...
