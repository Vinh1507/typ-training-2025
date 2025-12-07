INSERT INTO classes (name, code, description, semester, academic_year, created_at, updated_at)
VALUES ('CPRO1', 'C1', 'Introduction to C', 'Spring', '2025', NOW(), NOW())
ON CONFLICT (code) DO NOTHING;

INSERT INTO classes (name, code, description, semester, academic_year, created_at, updated_at)
VALUES ('CPRO2', 'C2', 'Introduction to C', 'Fall', '2025', NOW(), NOW())
ON CONFLICT (code) DO NOTHING;
