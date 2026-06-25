"""Local embedding strategy — sentence-transformers, runs in-process.

Zero per-call cost and fully offline, at the price of a heavy dependency (torch)
and RAM. The model is loaded lazily so importing this module stays cheap and the
container only pays the cost when `local` is the selected provider.
"""

import logging

log = logging.getLogger("nexus-ingest")

# mxbai-embed-large-v1 recommends this instruction for the *query* side only.
_QUERY_PROMPT = "Represent this sentence for searching relevant passages: "


class LocalEmbedder:
    def __init__(self, model_name: str, dim: int) -> None:
        from sentence_transformers import SentenceTransformer  # lazy: pulls torch

        # truncate_dim uses Matryoshka truncation to hit the schema's 1024 dim
        # exactly (mxbai is 1024 native, so this is a no-op there but keeps the
        # contract explicit if the model is swapped).
        self._model = SentenceTransformer(model_name, truncate_dim=dim)
        self._dim = dim
        log.info("LocalEmbedder ready: %s (dim=%d)", model_name, dim)

    @property
    def dimension(self) -> int:
        return self._dim

    def embed_documents(self, texts: list[str]) -> list[list[float]]:
        vecs = self._model.encode(texts, normalize_embeddings=True)
        return [v.tolist() for v in vecs]

    def embed_query(self, text: str) -> list[float]:
        vec = self._model.encode(_QUERY_PROMPT + text, normalize_embeddings=True)
        return vec.tolist()  # type: ignore[no-any-return]
