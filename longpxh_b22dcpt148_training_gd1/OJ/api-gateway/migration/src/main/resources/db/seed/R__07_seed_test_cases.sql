-- Test cases for Problem 1
INSERT INTO test_cases (problem_id, input, expected_output, is_sample, points, created_at)
SELECT 1, '2 7 11 15\n9', '0 1', TRUE, 10, NOW()
WHERE NOT EXISTS (SELECT 1 FROM test_cases WHERE problem_id = 1 AND input = '2 7 11 15\n9');

INSERT INTO test_cases (problem_id, input, expected_output, is_sample, points, created_at)
SELECT 1, '3 2 4\n6', '1 2', TRUE, 10, NOW()
WHERE NOT EXISTS (SELECT 1 FROM test_cases WHERE problem_id = 1 AND input = '3 2 4\n6');
