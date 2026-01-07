package com.example.qlsv_database.service;

import com.example.qlsv_database.model.Student;
import com.example.qlsv_database.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    public Student findById(Long id) {
        String cacheKey = "student:" + id;

        Object cachedObj = redisService.get(cacheKey);
        if (cachedObj != null) {
            return objectMapper.convertValue(cachedObj, Student.class);
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found: " + id));

        redisService.set(cacheKey, student, 1800);
        return student;
    }

    public Student addStudent(Student student) {
        Student saved = studentRepository.save(student);

        String cacheKey = "student: " + saved.getId();
        redisService.set(cacheKey, saved, 1800);

        return saved;
    }

    public Student updateStudent(Long id, Student sinhvien) {
        Student std =  findById(id);
        std.setName(sinhvien.getName());
        std.setAge(sinhvien.getAge());
        std.setMajor(sinhvien.getMajor());

        Student saved = studentRepository.save(std);

        String cacheKey = "student: " + saved.getId();
        redisService.delete(cacheKey);
        redisService.set(cacheKey, saved, 1800);

        return saved;
    }

    public void deleteStudent(Long id) {
        Student std = findById(id);
        studentRepository.delete(std);

        String cacheKey = "student: " + id;
        redisService.delete(cacheKey);
    }

    // bảng xếp hạng
    // Thêm, update điểm sinh viên
    public void updateScore(String studentId, double score){
        redisService.zAdd("student_ranking", score, studentId);
    }

    // Lấy top N sinh viên
    public Map<String, Double> getTopStudents(int n) {
        Set<ZSetOperations.TypedTuple<Object>> result =
                redisService.zRevRangeWithScores("student_ranking", 0, n - 1);

        Map<String, Double> ranking = new LinkedHashMap<>();

        if (result != null) {
            for (var tuple : result) {
                ranking.put(
                        "student " + tuple.getValue().toString(),
                        tuple.getScore()
                );
            }
        }
        return ranking;
    }
}
