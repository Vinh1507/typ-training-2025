package com.oj.platform.components.iam.infrastructure.persistence.jpa.repository;

import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.RoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpa, Long> {
    Optional<RoleJpa> findByName(String name);
}
