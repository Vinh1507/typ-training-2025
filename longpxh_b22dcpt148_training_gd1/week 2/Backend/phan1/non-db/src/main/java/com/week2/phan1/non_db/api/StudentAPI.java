package com.week2.phan1.non_db.api;


import com.week2.phan1.non_db.db.MyDatabase;
import com.week2.phan1.non_db.dto.StudentRequestDTO;
import com.week2.phan1.non_db.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentAPI {

    private final MyDatabase db;

    @GetMapping("/items")
    public List<Student> getStudents() {
        return db.getStudentList();
    }

    @GetMapping("/items/{id}")
    public Student getStudentById(@PathVariable Integer id) {
        return db.getStudentById(id);
    }

    @PostMapping("/items")
    public List<Student> createStudent(@RequestBody StudentRequestDTO dto) {
        db.createStudent(dto);
        return db.getStudentList();

    }

    @PutMapping("/items/{id}")
    public Student update(@PathVariable Integer id, @RequestBody StudentRequestDTO dto) {
        db.updateStudent(id, dto);
        return db.getStudentById(id);
    }

    @DeleteMapping("/items/{id}")
    public List<Student> delete(@PathVariable Integer id) {
        db.deleteStudent(id);
        return db.getStudentList();
    }


}

