package com.week2.phan2.db.hydrator;

import com.week2.phan2.db.dto.StudentRequestDTO;
import com.week2.phan2.db.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentHydrator {

    public Student toStudent(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }
}
