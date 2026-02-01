package com.oj_cpp.core.infra.persistence.jpa.repository;

import com.oj_cpp.core.infra.persistence.jpa.entity.ProblemJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ProblemJpaRepository extends JpaRepository<ProblemJpa, Long>, JpaSpecificationExecutor<ProblemJpa> {
    Optional<ProblemJpa> findByProblemCode(String problemCode);
    boolean existsByProblemCode(String problemCode);
    Optional<ProblemJpa> findTopByOrderByProblemCodeDesc();
}
