package com.example.qlsv_database.service;

import com.example.qlsv_database.config.RabbitMQConfig;
import com.example.qlsv_database.model.Student;
import com.example.qlsv_database.repository.StudentRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class StudentService {
    private final StudentRepository studentRepository;

    // Phần RabbitMQ - update phần addStudent
    // ----------------------------------------------
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    // ----------------------------------------------

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
        Student saved = studentRepository.save(student);

        // phần gửi message
        amqpTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                "New student created: " + saved.getId()
        );

        return saved;
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
