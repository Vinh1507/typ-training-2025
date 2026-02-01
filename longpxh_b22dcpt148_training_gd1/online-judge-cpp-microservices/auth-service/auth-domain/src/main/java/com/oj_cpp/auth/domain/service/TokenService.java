package com.oj_cpp.auth.domain.service;

import java.util.List;

public interface TokenService {
    String generateToken(String username, Long userId, List<String> roles);
    String generateRefreshToken(String username);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
