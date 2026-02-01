package com.oj_cpp.auth.infra.service;

import com.oj_cpp.auth.domain.model.Permission;
import com.oj_cpp.auth.domain.repository.PermissionRepository;
import com.oj_cpp.auth.domain.service.PermissionService;
import com.oj_cpp.auth.infra.persistence.jpa.entity.PermissionJpa;
import com.oj_cpp.auth.infra.persistence.jpa.entity.RoleJpa;
import com.oj_cpp.auth.infra.persistence.jpa.entity.RolePermissionJpa;
import com.oj_cpp.auth.infra.persistence.jpa.repository.PermissionJpaRepository;
import com.oj_cpp.auth.infra.persistence.jpa.repository.RoleJpaRepository;
import com.oj_cpp.auth.infra.persistence.jpa.repository.RolePermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    
    private final PermissionRepository permissionRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId);
    }

    @Override
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        RoleJpa role = roleJpaRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        PermissionJpa permission = permissionJpaRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        
        RolePermissionJpa rolePermission = new RolePermissionJpa();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        rolePermissionJpaRepository.save(rolePermission);
    }

    @Override
    @Transactional
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        rolePermissionJpaRepository.deleteByRoleIdAndPermissionId(roleId, permissionId);
    }
}
