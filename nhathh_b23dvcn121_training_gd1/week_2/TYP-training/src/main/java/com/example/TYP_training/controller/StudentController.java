package com.example.TYP_training.controller;

import com.example.TYP_training.entity.Student;
import com.example.TYP_training.service.StudentService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/all")
    public List<Student> findALl() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable Integer id) {
        return studentService.findById(id);
    }

    @DeleteMapping("{id}/delete")
    public String deleteById(@PathVariable Integer id) {
        return studentService.deleteById(id);
    }

    @PostMapping("/create")
    public String create() {
        return "Success.";
    }

    @PutMapping("/update")
    public String update() {
        return "Success.";
    }

}
