package com.week2.phan2.db.service;

import com.week2.phan2.db.dto.StudentRequestDTO;
import com.week2.phan2.db.hydrator.StudentHydrator;
import com.week2.phan2.db.model.Student;
import com.week2.phan2.db.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentHydrator studentHydrator;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student createStudent(StudentRequestDTO dto) {
        Student student = studentHydrator.toStudent(dto);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, StudentRequestDTO dto) {
        Student existingStudent = studentRepository.findById(id).orElse(null);
        if (existingStudent != null) {
            existingStudent.setName(dto.getName());
            existingStudent.setEmail(dto.getEmail());
            return studentRepository.save(existingStudent);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
