package com.oj.platform.components.iam.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordHasher {
    private final PasswordEncoder encoder;

    public String make(String password) {
        return encoder.encode(password);
    }

    public boolean check(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }
}
