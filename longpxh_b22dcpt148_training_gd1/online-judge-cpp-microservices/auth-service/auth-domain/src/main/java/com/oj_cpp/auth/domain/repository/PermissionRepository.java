package com.oj_cpp.auth.domain.repository;

import com.oj_cpp.auth.domain.model.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository {
    
    Optional<Permission> findByName(String name);
    
    Optional<Permission> findById(Long id);
    
    List<Permission> findByRoleId(Long roleId);
    
    Permission save(Permission permission);
    
    List<Permission> findAll();
}
