package com.oj_cpp.core.domain.repository;

import com.oj_cpp.core.domain.model.TestCase;

import java.util.List;
import java.util.Optional;

public interface TestCaseRepository {
    TestCase save(TestCase testCase);
    Optional<TestCase> findById(Long id);
    List<TestCase> findByProblemId(Long problemId);
    List<TestCase> findByProblemIdAndIsHidden(Long problemId, Boolean isHidden);
    void deleteById(Long id);
    void deleteByProblemId(Long problemId);
}
