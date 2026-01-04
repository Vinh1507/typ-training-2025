package com.fintrack.backend.config; // Hoặc package filters

import com.fintrack.backend.auth.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Bỏ qua filter cho các endpoint auth (login, register) - không cần token
        String path = request.getRequestURI();
        if (path != null && path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Kiểm tra header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Trích xuất Token và Email
        jwt = authHeader.substring(7); // Bỏ qua "Bearer "

        // Trong trường hợp Token bị lỗi (Expired, Invalid signature, etc.)
        try {
            userEmail = jwtService.extractEmail(jwt);
        } catch (Exception e) {
            // Log lỗi, nhưng vẫn cho phép filter chain tiếp tục (trả về 403 ở cuối chain)
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Xác thực người dùng (nếu có email và chưa được xác thực)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 4. Xác minh token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // 5. Cập nhật Security Context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Chuyển sang filter tiếp theo trong chuỗi
        filterChain.doFilter(request, response);
    }
}