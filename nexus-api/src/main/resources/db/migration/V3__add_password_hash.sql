-- bcrypt hashes are exactly 60 chars ($2b$<cost>$<22-char salt><31-char hash>).
-- Salt and cost are embedded in the string, so a single column suffices.
ALTER TABLE users ADD COLUMN password_hash VARCHAR(60) NOT NULL;

COMMENT ON COLUMN users.password_hash IS 'bcrypt hash (salt + cost embedded); never plaintext, never returned to clients';
