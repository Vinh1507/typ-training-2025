package com.oj.platform.components.submission.infrastructure.persistence.jpa.repository;

import com.oj.platform.components.submission.infrastructure.persistence.jpa.entity.SubmissionJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionJpaRepository extends JpaRepository<SubmissionJpa, Long> {
    Page<SubmissionJpa> findByUserId(Long userId, Pageable pageable);
    Page<SubmissionJpa> findByProblemId(Long problemId, Pageable pageable);
    Page<SubmissionJpa> findByUserIdAndProblemId(Long userId, Long problemId, Pageable pageable);
    List<SubmissionJpa> findByStatus(String status);

    @Query("SELECT s FROM SubmissionJpa s JOIN ProblemJpa p ON s.problemId = p.id " +
           "WHERE s.userId = :userId AND p.classId IN :classIds")
    Page<SubmissionJpa> findByUserIdAndProblemClassIdIn(
            @Param("userId") Long userId,
            @Param("classIds") List<Long> classIds,
            Pageable pageable);

    @Query("SELECT s FROM SubmissionJpa s JOIN ProblemJpa p ON s.problemId = p.id " +
           "WHERE s.problemId = :problemId AND s.userId = :userId AND p.classId IN :classIds")
    Page<SubmissionJpa> findByProblemIdAndUserIdAndProblemClassIdIn(
            @Param("problemId") Long problemId,
            @Param("userId") Long userId,
            @Param("classIds") List<Long> classIds,
            Pageable pageable);
}
