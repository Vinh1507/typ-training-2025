package com.oj_cpp.auth.infra.persistence.jpa.repository;

import com.oj_cpp.auth.infra.persistence.jpa.entity.RolePermissionJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionJpaRepository extends JpaRepository<RolePermissionJpa, RolePermissionJpa.RolePermissionId> {
    
    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
