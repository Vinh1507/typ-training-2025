package com.dailycode.dreamshop.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtAuthFilterConfig jwtAuthFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity hhtp) throws Exception{
        hhtp.csrf(c->c.disable())
        .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth->auth
        .requestMatchers("/auth/register","/auth/login","/products/**","/auth/refresh-token").permitAll()
        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
        .anyRequest().authenticated()
        )
        .cors(c->c.configurationSource(configurationSource()))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
         .exceptionHandling(ex -> ex
        .authenticationEntryPoint(jwtAuthEntryPoint)       // 401 handler
        .accessDeniedHandler(accessDeniedHandler)    // 403 handler
    )
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable);
        return hhtp.build();
    }
    private CorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Cho phép frontend (React/Vite/NextJS) truy cập
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        
        // ✅ Quan trọng: Cho phép gửi cookie kèm request
        config.setAllowCredentials(true);

        // ✅ Để cookie hiển thị được trong trình duyệt, cần chính xác domain (không dùng *)
        // Nếu dùng Vite thì thêm http://localhost:5173 vào đây

        // ✅ Tùy chọn: Thời gian cache preflight (OPTIONS)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
}

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth)throws Exception{
        return auth.getAuthenticationManager();
    }
}
