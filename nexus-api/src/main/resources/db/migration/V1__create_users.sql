CREATE TABLE users (
    id            UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(255) NOT NULL,
    display_name  VARCHAR(255) NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT users_email_unique UNIQUE (email)
);

CREATE INDEX idx_users_email ON users(email);

COMMENT ON TABLE  users              IS 'Nexus platform users';
COMMENT ON COLUMN users.id           IS 'UUID primary key — avoids sequential ID enumeration';
COMMENT ON COLUMN users.email        IS 'Unique login identifier';
COMMENT ON COLUMN users.display_name IS 'Human-readable name, not used for auth';
