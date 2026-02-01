package com.oj_cpp.auth.infra.persistence.jpa.repository;

import com.oj_cpp.auth.infra.persistence.jpa.entity.PermissionJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionJpa, Long> {
    
    Optional<PermissionJpa> findByName(String name);
    
    @Query("SELECT p FROM PermissionJpa p JOIN p.rolePermissions rp WHERE rp.role.id = :roleId")
    List<PermissionJpa> findByRoleId(@Param("roleId") Long roleId);
}
