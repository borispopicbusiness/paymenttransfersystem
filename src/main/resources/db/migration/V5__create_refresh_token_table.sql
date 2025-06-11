CREATE TABLE refresh_token (
                               id BIGSERIAL PRIMARY KEY,
                               token VARCHAR(255) UNIQUE NOT NULL,
                               expiry_date TIMESTAMP NOT NULL,
                               user_id BIGINT UNIQUE,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);