INSERT INTO problems (problem_code, title, content, level, time_limit, memory_limit, created_by, created_at, updated_at)
VALUES ('C001', 'Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.', 'EASY', 1000, 256, 1, NOW(), NOW())
ON CONFLICT (problem_code) DO NOTHING;

INSERT INTO problems (problem_code, title, content, level, time_limit, memory_limit, created_by, created_at, updated_at)
VALUES ('C002', 'Add Two Numbers', 'You are given two non-empty linked lists representing two non-negative integers.', 'MEDIUM', 1000, 256, 1, NOW(), NOW())
ON CONFLICT (problem_code) DO NOTHING;
