package com.team7.StemHub.dao;

import com.team7.StemHub.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepo extends JpaRepository<Course, UUID> {
    Optional<Course> findByCourseName(String courseName);

    List<Course> findByCourseNameContainingIgnoreCase(String keyword);

    List<Course> findByOtherNameContainingIgnoreCase(String keyword);

    List<Course> findByCourseIdContainingIgnoreCase(String keyword);

    List<Course> findAllByOrderByCourseId();
}
