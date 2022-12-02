CREATE TABLE IF NOT EXISTS "friend_requests" (
    self_id BIGINT NOT NULL,
    other_id BIGINT NOT NULL,
    FOREIGN KEY (self_id) REFERENCES "user"(id) ON DELETE CASCADE,
    FOREIGN KEY (other_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "friends" (
    self_id BIGINT NOT NULL,
    other_id BIGINT NOT NULL,
    FOREIGN KEY (self_id) REFERENCES "user"(id) ON DELETE CASCADE,
    FOREIGN KEY (other_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "blocked" (
     self_id BIGINT NOT NULL,
     other_id BIGINT NOT NULL,
     FOREIGN KEY (self_id) REFERENCES "user"(id) ON DELETE CASCADE,
     FOREIGN KEY (other_id) REFERENCES "user"(id) ON DELETE CASCADE
);