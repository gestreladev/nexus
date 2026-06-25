"""Deterministic fake embedder — no model, no network.

Used by tests and offline runs. Same text → same unit vector (via a hash), so a
query for a stored passage's exact text returns it with cosine ≈ 1. Lets the
whole pipeline (chunk → embed → upsert → search) be verified without torch or an
API key — the test-pyramid lesson's "fake over mock" applied to embeddings.
"""

import hashlib
import math


class FakeEmbedder:
    def __init__(self, dim: int = 1024) -> None:
        self._dim = dim

    @property
    def dimension(self) -> int:
        return self._dim

    def embed_documents(self, texts: list[str]) -> list[list[float]]:
        return [self._vector(t) for t in texts]

    def embed_query(self, text: str) -> list[float]:
        return self._vector(text)

    def _vector(self, text: str) -> list[float]:
        # Expand a SHA-256 digest into `dim` floats in [-1, 1], then L2-normalize.
        digest = hashlib.sha256(text.encode("utf-8")).digest()
        raw = [(digest[i % len(digest)] / 255.0) * 2.0 - 1.0 for i in range(self._dim)]
        norm = math.sqrt(sum(x * x for x in raw)) or 1.0
        return [x / norm for x in raw]
