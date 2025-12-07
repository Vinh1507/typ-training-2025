package com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.repository;

import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.entity.TeacherClassJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherClassJpaRepository extends JpaRepository<TeacherClassJpa, Long> {
    List<TeacherClassJpa> findByTeacherId(Long teacherId);
    List<TeacherClassJpa> findByClassId(Long classId);
    boolean existsByTeacherIdAndClassId(Long teacherId, Long classId);
    void deleteByTeacherIdAndClassId(Long teacherId, Long classId);
}
