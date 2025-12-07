package com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.repository;

import com.oj.platform.components.classmanagement.infrastructure.persistence.jpa.entity.ClassJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassJpaRepository extends JpaRepository<ClassJpa, Long> {
    Optional<ClassJpa> findByCode(String code);
    boolean existsByCode(String code);
}
