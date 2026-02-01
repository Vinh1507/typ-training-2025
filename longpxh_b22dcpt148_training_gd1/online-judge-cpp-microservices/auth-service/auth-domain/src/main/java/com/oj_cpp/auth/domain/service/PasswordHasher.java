package com.oj_cpp.auth.domain.service;

public interface PasswordHasher {
    
    String make(String plainPassword);
    
    boolean check(String plainPassword, String hashedPassword);
}
