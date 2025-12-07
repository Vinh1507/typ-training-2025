package com.oj.application.security;

import com.oj.platform.components.iam.domain.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        try {
            String subject = tokenService.extractSubject(jwt);

            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

                if (tokenService.validate(jwt, subject)) {
                    // Extract user_id and class_ids from JWT claims
                    Map<String, Object> claims = tokenService.parseClaims(jwt);
                    Long userId = extractUserId(claims);
                    List<Long> classIds = extractClassIds(claims);

                    // Wrap UserDetails with ClassAwareUser to carry user_id and class_ids
                    Object principal = userDetails;
                    if (userId != null) {
                        principal = new ClassAwareUser(userDetails, userId, classIds);
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            principal, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token invalid, continue without authentication
        }

        filterChain.doFilter(request, response);
    }

    private Long extractUserId(Map<String, Object> claims) {
        Object userIdObj = claims.get("user_id");
        if (userIdObj == null) {
            return null;
        }
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        if (userIdObj instanceof String) {
            try {
                return Long.parseLong((String) userIdObj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Long> extractClassIds(Map<String, Object> claims) {
        Object classIdsObj = claims.get("class_ids");
        if (classIdsObj == null) {
            return List.of();
        }
        
        List<Long> result = new ArrayList<>();
        if (classIdsObj instanceof List) {
            for (Object item : (List<?>) classIdsObj) {
                if (item instanceof Number) {
                    result.add(((Number) item).longValue());
                } else if (item instanceof String) {
                    try {
                        result.add(Long.parseLong((String) item));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        return result;
    }
}
