package com.oj_cpp.core.infra.persistence.jpa.repository;

import com.oj_cpp.core.infra.persistence.jpa.entity.TestCaseJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseJpaRepository extends JpaRepository<TestCaseJpa, Long> {
    List<TestCaseJpa> findByProblemId(Long problemId);
    List<TestCaseJpa> findByProblemIdAndIsHidden(Long problemId, Boolean isHidden);
    void deleteByProblemId(Long problemId);
}
