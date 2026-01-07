package com.example.qlsv_database.service;

import com.example.qlsv_database.model.Student;
import com.example.qlsv_database.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found: " + id));
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student sinhvien) {
        Student std =  findById(id);
        std.setName(sinhvien.getName());
        std.setAge(sinhvien.getAge());
        std.setMajor(sinhvien.getMajor());
        return studentRepository.save(std);
    }

    public void deleteStudent(Long   id) {
        Student std = findById(id);
        studentRepository.delete(std);
    }
}
