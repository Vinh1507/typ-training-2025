package com.oj_cpp.auth.domain.repository;

import com.oj_cpp.auth.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findById(Long id);
    
    User save(User user);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    void deleteById(Long id);
}
