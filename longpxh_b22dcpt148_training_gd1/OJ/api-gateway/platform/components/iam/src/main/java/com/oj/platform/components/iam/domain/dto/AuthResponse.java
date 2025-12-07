package com.oj.platform.components.iam.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private OffsetDateTime accessTokenExpiredAt;
    private OffsetDateTime refreshTokenExpiredAt;
    private String tokenType;
    
    // User info
    private Long userId;
    private String username;
    private String fullName;
    private java.util.List<String> roles;
}
