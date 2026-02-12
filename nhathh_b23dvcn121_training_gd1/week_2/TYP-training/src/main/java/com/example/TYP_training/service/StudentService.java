package com.example.TYP_training.service;

import com.example.TYP_training.entity.Student;
import com.example.TYP_training.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Integer id) {
        Student student = studentRepository.findById(id);
        if (student == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Not found student with id = " + id);
        }
        return student;
    }

    public String deleteById(Integer id) {
        return studentRepository.deleteById(id);
    }
}
