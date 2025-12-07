package com.week2.phan1.non_db.db;

import com.week2.phan1.non_db.dto.StudentRequestDTO;
import com.week2.phan1.non_db.model.Student;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class MyDatabase {

    private static List<Student> studentList = new ArrayList<>();

    public static void init(){
        Student student1 =  new Student(1, "Long", "long@gmail.com");
        Student student2 =  new Student(2, "Nam", "nam@gmail.com");
        Student student3 =  new Student(3, "An", "an@gmail.com");
        Student student4 =  new Student(4, "Hoa", "hoa@gmail.com");
        Student student5 =  new Student(5, "Lan", "lan@gmail.com");

        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentList.add(student4);
        studentList.add(student5);
        System.out.println(">> INIT list: " + studentList);
    }

    public List<Student> getStudentList() {
        return studentList;
    }



    public Student getStudentById(Integer id) {
        for(Student s : studentList) {
            if(s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public void createStudent(StudentRequestDTO dto) {
        Student stu = new Student(dto.getName(), dto.getEmail());
        stu.setId(studentList.size() + 1);
        studentList.add(stu);
    }

    public void updateStudent(Integer id, StudentRequestDTO dto) {
        Student stu = getStudentById(id);
        if (stu != null) {
            stu.setName(dto.getName());
            stu.setEmail(dto.getEmail());
        }
    }

    public void deleteStudent(Integer id) {
        Student stu = getStudentById(id);
        if (stu != null) {
            studentList.remove(stu);
        }
    }

}
