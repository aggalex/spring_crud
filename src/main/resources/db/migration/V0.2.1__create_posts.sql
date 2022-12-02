CREATE TABLE IF NOT EXISTS "posts" (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    body VARCHAR(10000) NOT NULL,
    poster BIGINT,
    parent BIGINT,
    likes BIGINT DEFAULT 0,
    dislikes BIGINT DEFAULT 0,
    views BIGINT DEFAULT 0,
    deleted BOOLEAN DEFAULT false,
    private BOOLEAN DEFAULT false,

    FOREIGN KEY(poster) REFERENCES "user"(id) ON DELETE SET NULL,
    FOREIGN KEY(parent) REFERENCES "posts"(id) ON DELETE CASCADE
)