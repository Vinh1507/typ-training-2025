package com.oj_cpp.judge.domain.repository;

import com.oj_cpp.judge.domain.model.TestCase;
import java.util.List;

public interface TestCaseRepository {
    List<TestCase> findByProblemId(Long problemId);
}
