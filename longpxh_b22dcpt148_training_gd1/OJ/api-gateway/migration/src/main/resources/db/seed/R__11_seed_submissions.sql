-- Submission for User 2, Problem 1
INSERT INTO submissions (user_id, problem_id, code, language, verdict, status, execution_time, memory_used, submitted_at)
SELECT 2, 1, 'printf("Hello")', 'C++', 'AC', 'COMPLETED', 10, 1024, NOW()
WHERE NOT EXISTS (SELECT 1 FROM submissions WHERE user_id = 2 AND problem_id = 1 AND code = 'printf("Hello")');
