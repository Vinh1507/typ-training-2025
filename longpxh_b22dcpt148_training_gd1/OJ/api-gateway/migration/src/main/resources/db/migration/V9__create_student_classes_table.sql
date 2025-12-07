CREATE TABLE student_classes (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    class_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_student_classes_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_student_classes_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE
);
