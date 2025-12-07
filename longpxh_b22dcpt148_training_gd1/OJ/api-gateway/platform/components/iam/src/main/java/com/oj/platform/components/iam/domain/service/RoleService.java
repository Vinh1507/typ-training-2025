package com.oj.platform.components.iam.domain.service;

import com.oj.platform.components.iam.domain.model.role.Role;
import com.oj.platform.components.iam.domain.model.role.RoleInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleInterface roleRepository;

    public RoleService(RoleInterface roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        return roleRepository.createRole(role);
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.getRoleById(id);
    }

    public Optional<Role> getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }

    public List<Role> getAllRoles() {
        return roleRepository.getAllRoles();
    }

    public Role updateRole(Role role) {
        return roleRepository.updateRole(role);
    }

    public boolean deleteRole(Long id) {
        return roleRepository.deleteRole(id);
    }
}
