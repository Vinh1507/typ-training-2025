package com.team7.StemHub.service;

import com.team7.StemHub.dao.CourseRepo;
import com.team7.StemHub.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepo courseRepo;

    public Course getCourseByCourseName(String courseName) {
        return courseRepo.findByCourseName(courseName)
                .orElseGet(() -> {
                    return courseRepo.findByCourseName("Khác").orElse(null);
                });
    }

    public List<Course> getAllCourses(){
        return courseRepo.findAllByOrderByCourseId();
    }

    public Set<Course> searchCourses(String keyword){
        Set<Course> result = new java.util.HashSet<>();
        List<Course> byCourseName = courseRepo.findByCourseNameContainingIgnoreCase(keyword);
        List<Course> byOther = courseRepo.findByOtherNameContainingIgnoreCase(keyword);
        List<Course> byId = courseRepo.findByCourseIdContainingIgnoreCase(keyword);
        result.addAll(byCourseName);
        result.addAll(byOther);
        result.addAll(byId);
        return result;
    }
}
