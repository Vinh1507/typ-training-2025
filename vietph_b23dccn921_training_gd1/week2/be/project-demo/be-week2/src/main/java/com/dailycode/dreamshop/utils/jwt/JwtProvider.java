package com.dailycode.dreamshop.utils.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtProvider {
    @Value("${spring.security.jwt.secret-key}")
    private String JWT_SECRET;
    @Value("${spring.security.jwt.expired}")
    private String JWT_EXPIRED;
    public boolean validateToken(String token) {
    try {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);

        return true; 

    } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
       System.out.println("Invalid JWT signature: "+ e.getMessage());
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
        System.out.println("JWT token is expired: "+ e.getMessage());
    } catch (io.jsonwebtoken.UnsupportedJwtException e) {
        System.out.println("JWT token is unsupported: "+ e.getMessage());
    } catch (IllegalArgumentException e) {
        System.out.println("JWT claims string is empty: "+ e.getMessage());
    }

    return false;
}
    public String getEmailFromToken(String token){
        String email=null;
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
        try {
            if(validateToken(token))
            {
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
             email= claims.getSubject();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return email;
    }
    private String getRoleFromToken(String token){
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
                return claims.get("roles", String.class);
         
    }
    public String getTokenFromheaders(HttpServletRequest http)
    {
        String token=null;
        String authHeader = http.getHeader("Authorization");
        if(authHeader!=null && authHeader.startsWith("Bearer "))
        {
            token = authHeader.substring(7);
        }
        return token;
        

    }
    private String getRole(UserDetails userDetails){
        return userDetails.getAuthorities().stream().map(auth->
        auth.getAuthority()
        )
        .collect(Collectors.joining(","));
    }
    public String refreshToken(HttpServletRequest request){
        Cookie []cookies = request.getCookies();
        String token = null;
        for(Cookie c : cookies)
        {
            if(c.getName().equals("refresh_token"))
            {
                token = c.getValue();
            }
        }
        if(validateToken(token))
        { SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
            String email = getEmailFromToken(token);
            String role  = getRoleFromToken(token);
            token = Jwts.builder().setIssuedAt(new Date())
        .setSubject(email)
        .claim("roles",role)
        .setExpiration(new Date(System.currentTimeMillis() +Long.parseLong(JWT_EXPIRED)))
        .signWith(key,SignatureAlgorithm.HS256).compact();
        }
        return token;
    }
    
    public String generateAccessToken(UserDetails userDetails)
    {
        String email= userDetails.getUsername();
        String role= getRole(userDetails);
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
        return Jwts.builder().setIssuedAt(new Date())
        .setSubject(email)
        .claim("roles",role)
        .setExpiration(new Date(System.currentTimeMillis() +Long.parseLong(JWT_EXPIRED)))
        .signWith(key,SignatureAlgorithm.HS256).compact();
        
    }
    public String generateRefreshToken(UserDetails userDetails)
    {
        String email= userDetails.getUsername();
        String role= getRole(userDetails);
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
        return Jwts.builder().setIssuedAt(new Date())
        .setSubject(email)
        .claim("roles",role)
        .setExpiration(new Date(System.currentTimeMillis() + (86400*7)))
        .signWith(key,SignatureAlgorithm.HS256).compact();
        
    }
}
