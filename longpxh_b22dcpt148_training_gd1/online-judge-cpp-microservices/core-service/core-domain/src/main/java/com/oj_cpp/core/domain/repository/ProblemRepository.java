package com.oj_cpp.core.domain.repository;

import com.oj_cpp.core.domain.model.Problem;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository {
    Problem save(Problem problem);
    Optional<Problem> findById(Long id);
    Optional<Problem> findByProblemCode(String problemCode);
    void deleteById(Long id);
    Optional<Problem> findTopByOrderByProblemCodeDesc();
    boolean existsByProblemCode(String problemCode);
    List<Problem> search(String keyword, String code, String title, String level, Long classId);
}
