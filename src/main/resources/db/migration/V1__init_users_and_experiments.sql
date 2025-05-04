CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    subscription_plan VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS experiments (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS user_experiments (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    experiment_id INT NOT NULL REFERENCES experiments(id),
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
