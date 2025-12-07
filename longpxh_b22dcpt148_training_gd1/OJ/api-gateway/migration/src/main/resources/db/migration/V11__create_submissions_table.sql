CREATE TABLE submissions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    problem_id BIGINT NOT NULL,
    code TEXT NOT NULL,
    language VARCHAR(20) NOT NULL,
    verdict VARCHAR(20),
    status VARCHAR(20),
    execution_time INTEGER,
    memory_used INTEGER,
    submitted_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_submissions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_submissions_problem FOREIGN KEY (problem_id) REFERENCES problems(id) ON DELETE CASCADE
);
