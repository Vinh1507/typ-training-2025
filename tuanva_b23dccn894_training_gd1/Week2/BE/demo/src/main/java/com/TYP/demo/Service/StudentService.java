package com.TYP.demo.Service;

import com.TYP.demo.Model.Student;
import com.TYP.demo.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student newStudent) {
        return studentRepository.findById(id)
                .map(existing -> {
                    existing.setName(newStudent.getName());
                    existing.setAge(newStudent.getAge());
                    existing.setEmail(newStudent.getEmail());
                    return studentRepository.save(existing);
                })
                .orElse(null);
    }

    public boolean delete(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
