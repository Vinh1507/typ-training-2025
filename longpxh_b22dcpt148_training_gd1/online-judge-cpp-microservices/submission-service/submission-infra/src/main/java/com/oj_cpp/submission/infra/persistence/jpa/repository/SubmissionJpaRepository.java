package com.oj_cpp.submission.infra.persistence.jpa.repository;

import com.oj_cpp.submission.infra.persistence.jpa.entity.SubmissionJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionJpaRepository extends JpaRepository<SubmissionJpa, Long> {
    Page<SubmissionJpa> findByUserId(Long userId, Pageable pageable);
}
