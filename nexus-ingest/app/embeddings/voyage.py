"""Voyage AI embedding strategy — hosted API, production-grade quality.

Keeps the container slim (just an HTTP call) at the price of a per-call cost, a
network dependency, and an API key. `output_dimension` is pinned to the schema
dim so it stays interchangeable with the local model.
"""

import logging

log = logging.getLogger("nexus-ingest")


class VoyageEmbedder:
    def __init__(self, model: str, dim: int, api_key: str) -> None:
        import voyageai  # lazy import

        # api_key="" → fall back to the VOYAGE_API_KEY env the SDK reads itself.
        self._client = voyageai.Client(api_key=api_key or None)
        self._model = model
        self._dim = dim
        log.info("VoyageEmbedder ready: %s (dim=%d)", model, dim)

    @property
    def dimension(self) -> int:
        return self._dim

    def embed_documents(self, texts: list[str]) -> list[list[float]]:
        result = self._client.embed(
            texts, model=self._model, input_type="document", output_dimension=self._dim
        )
        return list(result.embeddings)

    def embed_query(self, text: str) -> list[float]:
        result = self._client.embed(
            [text], model=self._model, input_type="query", output_dimension=self._dim
        )
        return list(result.embeddings[0])
