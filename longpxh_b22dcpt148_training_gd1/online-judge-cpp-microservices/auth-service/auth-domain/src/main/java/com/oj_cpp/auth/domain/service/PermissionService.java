package com.oj_cpp.auth.domain.service;

import com.oj_cpp.auth.domain.model.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    
    Optional<Permission> findByName(String name);
    
    Optional<Permission> findById(Long id);
    
    List<Permission> findByRoleId(Long roleId);
    
    Permission createPermission(Permission permission);
    
    void assignPermissionToRole(Long roleId, Long permissionId);
    
    void removePermissionFromRole(Long roleId, Long permissionId);
}
