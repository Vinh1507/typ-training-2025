package ptit.edu.vn.bookshop.util.security;

import ptit.edu.vn.bookshop.domain.dto.response.LoginResponseDTO;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityUtil {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Value("${app.jwt.base64-secret}")
    private String jwtKey;

    @Value("${app.jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    @Value("${app.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;


    public String createAccessToken(String email, LoginResponseDTO dto) {
        LoginResponseDTO.UserInsideToken userToken = new LoginResponseDTO.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setName(dto.getUser().getName());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setStatus(dto.getUser().getStatus());

        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + dto.getUser().getRole().getName());
        //@formater: off
        // Tao phan payload
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("roles", roles)
                .claim("user", userToken)
                .build();

        // tao phan header goi ra thuat toan
        JwsHeader jwtHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwtHeader, claims)).getTokenValue();
    }

    public String createRefreshToken(String email, LoginResponseDTO dto) {
        LoginResponseDTO.UserInsideToken userToken = new LoginResponseDTO.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setName(dto.getUser().getName());
        userToken.setEmail(dto.getUser().getEmail());

        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        //@formater: off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .build();

        // tao phan header goi ra thuat toan
        JwsHeader jwtHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwtHeader, claims)).getTokenValue();
    }

    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> REFRESH TOKEN  error: " + e.getMessage());
            throw e;
        }

    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

}
