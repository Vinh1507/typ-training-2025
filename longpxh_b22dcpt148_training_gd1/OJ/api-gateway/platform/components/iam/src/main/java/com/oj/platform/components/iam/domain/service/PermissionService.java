package com.oj.platform.components.iam.domain.service;

import com.oj.platform.components.iam.domain.model.permission.Permission;
import com.oj.platform.components.iam.domain.model.permission.PermissionInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionInterface permissionRepository;

    public PermissionService(PermissionInterface permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.createPermission(permission);
    }

    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.getPermissionById(id);
    }

    public Optional<Permission> getPermissionByName(String name) {
        return permissionRepository.getPermissionByName(name);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.getAllPermissions();
    }

    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionRepository.getPermissionsByRoleId(roleId);
    }

    public Permission updatePermission(Permission permission) {
        return permissionRepository.updatePermission(permission);
    }

    public boolean deletePermission(Long id) {
        return permissionRepository.deletePermission(id);
    }
}
