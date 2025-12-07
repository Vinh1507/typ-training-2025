package com.oj.platform.components.iam.infrastructure.service;

import com.oj.platform.components.iam.domain.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenServiceImpl implements TokenService {
    private final SecretKey secretKey;

    public JwtTokenServiceImpl(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generate(String subject, Map<String, Object> claims, long ttlMillis) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + ttlMillis);

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String extractSubject(String token) {
        return parseAllClaims(token).getSubject();
    }

    @Override
    public boolean validate(String token, String subject) {
        try {
            Claims claims = parseAllClaims(token);
            return claims.getSubject().equals(subject) && !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> parseClaims(String token) {
        return new HashMap<>(parseAllClaims(token));
    }

    public Claims parseAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
