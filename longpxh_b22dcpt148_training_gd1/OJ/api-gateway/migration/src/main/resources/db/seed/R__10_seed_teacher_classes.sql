-- Admin (1) assigned to Algorithms 101 (1)
INSERT INTO teacher_classes (teacher_id, class_id, assigned_at)
SELECT 1, 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM teacher_classes WHERE teacher_id = 1 AND class_id = 1);
