package com.oj.platform.components.iam.domain.model.permission;

import com.oj.platform.components.iam.domain.model.permission.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionInterface {
    Permission createPermission(Permission permission);
    
    Optional<Permission> getPermissionById(Long id);
    
    Optional<Permission> getPermissionByName(String name);
    
    List<Permission> getAllPermissions();
    
    List<Permission> getPermissionsByRoleId(Long roleId);
    
    Permission updatePermission(Permission permission);
    
    boolean deletePermission(Long id);
}
