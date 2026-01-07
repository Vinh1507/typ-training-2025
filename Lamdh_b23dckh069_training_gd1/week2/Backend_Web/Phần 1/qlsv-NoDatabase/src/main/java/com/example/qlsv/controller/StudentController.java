package com.example.qlsv.controller;

import com.example.qlsv.model.Student;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
     private List<Student> students = new ArrayList<>();
     private int id = 1;

     @GetMapping
     public List<Student> getAllStudents(){
         return students;
     }

     @GetMapping("/{id}")
     public Student getStudentById(@PathVariable int id){
         Optional<Student> opt = students.stream().filter(s -> s.getId() == id).findFirst();
         return opt.orElse(null);
     }

     @PostMapping
     public Student addStudent(@RequestBody Student student){
         student.setId(id++);
         students.add(student);
         return student;
     }

     @PutMapping("/{id}")
     public Student updateStudent(@PathVariable int id, @RequestBody Student updateStudent){
         for (Student s :  students){
             if(s.getId() == id){
                 s.setName(updateStudent.getName());
                 s.setAge(updateStudent.getAge());
                 s.setNganhhoc(updateStudent.getNganhhoc());
                 return s;
             }
         }
         return null;
     }

    @DeleteMapping("/{id}")
    public Student deleteStudent(@PathVariable int id){
        Optional<Student> student = students.stream().filter(s -> s.getId() == id).findFirst();
        if(student.isPresent()){
            students.remove(student.get());
            return student.get();
        } else {
            return null; // hoặc trả ResponseEntity.notFound().build()
        }
    }
}
