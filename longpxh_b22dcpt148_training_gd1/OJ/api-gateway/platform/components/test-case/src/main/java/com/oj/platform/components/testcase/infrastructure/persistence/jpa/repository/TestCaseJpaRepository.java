package com.oj.platform.components.testcase.infrastructure.persistence.jpa.repository;

import com.oj.platform.components.testcase.infrastructure.persistence.jpa.entity.TestCaseJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseJpaRepository extends JpaRepository<TestCaseJpa, Long> {
    List<TestCaseJpa> findByProblemId(Long problemId);
    List<TestCaseJpa> findByProblemIdAndIsSample(Long problemId, Boolean isSample);
    void deleteByProblemId(Long problemId);
}
