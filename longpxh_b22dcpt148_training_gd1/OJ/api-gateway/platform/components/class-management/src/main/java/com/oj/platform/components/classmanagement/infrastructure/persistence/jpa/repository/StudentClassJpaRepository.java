package com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.repository;

import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.entity.StudentClassJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentClassJpaRepository extends JpaRepository<StudentClassJpa, Long> {
    List<StudentClassJpa> findByStudentId(Long studentId);
    List<StudentClassJpa> findByClassId(Long classId);
    Optional<StudentClassJpa> findByStudentIdAndClassId(Long studentId, Long classId);
    boolean existsByStudentIdAndClassId(Long studentId, Long classId);
    void deleteByStudentIdAndClassId(Long studentId, Long classId);
}
