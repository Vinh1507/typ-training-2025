package com.oj_cpp.judge.infra.persistence.jpa.repository;

import com.oj_cpp.judge.infra.persistence.jpa.entity.TestCaseJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCaseJpaRepository extends JpaRepository<TestCaseJpa, Long> {

    List<TestCaseJpa> findByProblemId(Long problemId);


}
