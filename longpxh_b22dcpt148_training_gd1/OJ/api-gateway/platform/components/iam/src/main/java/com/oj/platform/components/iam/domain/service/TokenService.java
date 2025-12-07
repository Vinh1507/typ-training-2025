package com.oj.platform.components.iam.domain.service;

import java.util.Map;

public interface TokenService {
    String generate(String subject, Map<String, Object> claims, long ttlMillis);
    
    String extractSubject(String token);
    
    boolean validate(String token, String subject);
    
    Map<String, Object> parseClaims(String token);
}
