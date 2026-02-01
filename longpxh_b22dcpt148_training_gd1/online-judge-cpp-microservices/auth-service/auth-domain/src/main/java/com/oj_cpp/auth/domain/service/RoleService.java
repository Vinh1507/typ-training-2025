package com.oj_cpp.auth.domain.service;

import com.oj_cpp.auth.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    
    Optional<Role> findByName(String name);
    
    Optional<Role> findById(Long id);
    
    List<Role> findByUserId(Long userId);
    
    Role createRole(Role role);
    
    void assignRoleToUser(Long userId, Long roleId);
    
    void removeRoleFromUser(Long userId, Long roleId);
}
