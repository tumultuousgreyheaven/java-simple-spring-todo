CREATE TABLE IF NOT EXISTS tasks (
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(50) NOT NULL DEFAULT 'TODO',
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);