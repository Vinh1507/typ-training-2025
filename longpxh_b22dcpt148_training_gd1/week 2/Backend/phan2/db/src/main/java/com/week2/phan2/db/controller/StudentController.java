package com.week2.phan2.db.controller;


import com.week2.phan2.db.dto.StudentRequestDTO;
import com.week2.phan2.db.model.Student;
import com.week2.phan2.db.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/items")
    public List<Student> getAlls(){
        return studentService.getAllStudents();
    }

    @GetMapping("/items/{id}")
    public Student getStudent(@PathVariable int id){
        return studentService.getStudentById((long) id);
    }

    @PostMapping("/items")
    public Student createStudent(@RequestBody StudentRequestDTO dto){
        return studentService.createStudent(dto);
    }

    @PutMapping("/items/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody StudentRequestDTO dto){
        return studentService.updateStudent(id, dto);
    }

    @DeleteMapping("/items/{id}")
    public void deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
    }




}
