-- User (2) enrolled in Algorithms 101 (1)
INSERT INTO student_classes (student_id, class_id, enrolled_at)
SELECT 2, 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM student_classes WHERE student_id = 2 AND class_id = 1);
