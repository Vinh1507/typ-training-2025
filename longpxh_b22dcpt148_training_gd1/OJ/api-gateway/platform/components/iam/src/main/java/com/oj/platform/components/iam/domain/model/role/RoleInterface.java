package com.oj.platform.components.iam.domain.model.role;

import com.oj.platform.components.iam.domain.model.role.Role;

import java.util.List;
import java.util.Optional;

public interface RoleInterface {
    Role createRole(Role role);
    
    Optional<Role> getRoleById(Long id);
    
    Optional<Role> getRoleByName(String name);
    
    List<Role> getAllRoles();
    
    Role updateRole(Role role);
    
    boolean deleteRole(Long id);
}
