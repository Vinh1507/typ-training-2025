package com.bookingcare.utils;

import com.bookingcare.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private int expiration = 2592000;
    private String secretKey = "TaqlmGv1iEDMRiFp/pHuID1+T84IABfuA0xXh4GhiUI=";
//    signature = HMACSHA256(
//            base64UrlEncode(header) + "." + base64UrlEncode(payload),
//    secretKey
//    )
    // client gui lai token, dung secretkey de tinh lai signature
    // Neu signature khop => dung token do server tao => pass. Con khong khop la no da bi thay doi => tu troi truy cap

    public String generateToken(UserEntity userEntity) {
        Map<String, Object> claims = new HashMap<>(); // map chua thong tin payload
        claims.put("userName", userEntity.getUsername());
        try{
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userEntity.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration*1000L)) // han trong 30 ngay
                    .signWith(getSignKey(), SignatureAlgorithm.HS256) // tao signature
                    .compact();
            return token;
        }catch(Exception e){
            throw new InvalidParameterException("Cannot generate Jwt Token, error: " + e.getMessage());
        }
    }

    // ma hoa key ve dang nhi phan
    private Key getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    // giai ma xac thuc token, tra ve thong tin ng dung trong payload
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // gan secretKey
                .build()
                .parseClaimsJws(token)// giai ma token, check signature
                .getBody(); // lay ra payload
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // check expiration
    public boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // check han cua token
    }
}
