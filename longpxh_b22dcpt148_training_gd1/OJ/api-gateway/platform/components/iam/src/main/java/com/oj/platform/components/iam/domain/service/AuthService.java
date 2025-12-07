package com.oj.platform.components.iam.domain.service;

import com.oj.platform.components.iam.domain.dto.AuthResponse;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.UserJpa;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TTL = 15 * 60 * 1000; // 15 minutes
    private static final long REFRESH_TTL = 7 * 24 * 60 * 60 * 1000; // 7 days

    private final UserJpaRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    public AuthResponse login(String username, String password, Long userId, List<Long> classIds) {
        UserJpa user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordHasher.check(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Create claims with user_id and class_ids
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", userId != null ? userId : user.getId());
        claims.put("class_ids", classIds != null ? classIds : List.of());

        AuthResponse response = createTokenPair(username, claims);

        // Fill user info
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setRoles(List.of("ROLE_USER", "ROLE_ADMIN")); 
        
        return response;
    }

    public AuthResponse refreshToken(String refreshToken) {
        String subject = tokenService.extractSubject(refreshToken);
        
        if (!tokenService.validate(refreshToken, subject)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        Map<String, Object> claims = tokenService.parseClaims(refreshToken);
        return createTokenPair(subject, claims);
    }

    private AuthResponse createTokenPair(String subject, Map<String, Object> extraClaims) {
        long now = System.currentTimeMillis();

        Map<String, Object> accessClaims = new HashMap<>(extraClaims);
        accessClaims.put("token_type", TOKEN_TYPE_ACCESS);
        String accessToken = tokenService.generate(subject, accessClaims, ACCESS_TTL);

        Map<String, Object> refreshClaims = new HashMap<>(extraClaims);
        refreshClaims.put("token_type", TOKEN_TYPE_REFRESH);
        String refreshToken = tokenService.generate(subject, refreshClaims, REFRESH_TTL);

        OffsetDateTime accessExp = OffsetDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(now + ACCESS_TTL), ZoneOffset.UTC);
        OffsetDateTime refreshExp = OffsetDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(now + REFRESH_TTL), ZoneOffset.UTC);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiredAt(accessExp)
                .refreshTokenExpiredAt(refreshExp)
                .tokenType(BEARER_TYPE)
                .build();
    }
}

