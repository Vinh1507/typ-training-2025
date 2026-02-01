package com.oj_cpp.auth.infra.security;

import com.oj_cpp.auth.domain.service.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordHasherImpl implements PasswordHasher {
    
    private final PasswordEncoder passwordEncoder;

    @Override
    public String make(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean check(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}
