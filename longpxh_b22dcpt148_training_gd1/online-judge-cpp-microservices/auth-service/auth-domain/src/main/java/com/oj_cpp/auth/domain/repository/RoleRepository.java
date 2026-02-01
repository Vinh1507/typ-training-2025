package com.oj_cpp.auth.domain.repository;

import com.oj_cpp.auth.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    
    Optional<Role> findByName(String name);
    
    Optional<Role> findById(Long id);
    
    List<Role> findByUserId(Long userId);
    
    Role save(Role role);
    
    List<Role> findAll();
}
