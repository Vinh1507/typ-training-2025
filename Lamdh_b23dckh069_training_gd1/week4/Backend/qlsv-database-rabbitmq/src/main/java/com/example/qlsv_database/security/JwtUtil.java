package com.example.qlsv_database.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "this_is_a_random_secret_key_Lorem Ipsum is simply dummy text of the printing and typesetting industry";
    private static final long EXPIRATION_TIME = 10 * 60 * 1000; // thời gian hết hạn access token
    private static final long REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;// thời gian hết hạn refresh token

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // sinh token từ user
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return createToken(claims, userDetails.getUsername(), EXPIRATION_TIME);
    }

    // tạo refresh token
    public String generateRefreshToken(UserDetails userDetails){
        return createToken(new HashMap<>(), userDetails.getUsername(), REFRESH_EXPIRATION_TIME);
    }
    private String createToken(Map<String, Object> claims, String subject, long expiration){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // lấy username từ token
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }
    // lấy role từ token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    // ktra token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
