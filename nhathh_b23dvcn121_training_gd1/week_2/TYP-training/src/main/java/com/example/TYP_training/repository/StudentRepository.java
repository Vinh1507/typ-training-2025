package com.example.TYP_training.repository;

import com.example.TYP_training.entity.Student;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentRepository {
    static Student student1 = new Student(1, "B23CN01", "Nguyễn Văn A", "0900124123", "Hà Nội");
    static Student student2 = new Student(2, "B23CN02", "Hà Văn A", "0963124123", "Hà Tĩnh");
    static List<Student> studentList = new ArrayList<>(List.of(student1, student2));

    public List<Student> findAll() {
        return studentList;
    }

    public Student findById(Integer id) {
        for (Student s: studentList) {
            if (Objects.equals(s.getId(), id)) {
                return s;
            }
        }
        return null;
    }

    public String deleteById(Integer id) {
        for (Student s: studentList) {
            if (Objects.equals(s.getId(), id)) {
                if (studentList.remove(s)) {
                    return "Success.";
                }
            }
        }
        throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Not found student with id = " + id);
    }
}
