package com.oj_cpp.auth.domain.service;

import com.oj_cpp.auth.domain.model.User;

import java.util.Optional;

public interface UserService {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findById(Long id);
    
    User createUser(User user);
    
    User updateUser(User user);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
