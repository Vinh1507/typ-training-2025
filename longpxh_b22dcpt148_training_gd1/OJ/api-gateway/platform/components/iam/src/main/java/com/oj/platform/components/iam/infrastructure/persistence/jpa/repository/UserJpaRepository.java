package com.oj.platform.components.iam.infrastructure.persistence.jpa.repository;

import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpa, Long> {
    Optional<UserJpa> findByUsername(String username);
    Optional<UserJpa> findByEmail(String email);
}
