package com.bookingcare.configuration;

import com.bookingcare.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    // v2 check phan quyen
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // check truoc khi kiem tra token
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers("/api/users/register", "/api/users/login","/api/users/forgot-password",
                                    "/api/users/verify-otp","/api/users/reset-password","/api/chat", "/api/chat-with-image","/api/payment/**", "/api/message/send").permitAll()
                            .requestMatchers(GET, "/api/admin/users", "/api/admin/users/*").hasRole("ADMIN")
                            .requestMatchers(DELETE, "/api/admin/users/*").hasRole("ADMIN")
                            .requestMatchers(PUT,"/api/admin/users/*").hasRole("ADMIN")
                            .requestMatchers(POST, "/api/admin/users", "/api/admin/users/*").hasRole("ADMIN")
                            .requestMatchers(GET, "/api/admin/doctors", "/api/admin/doctors/*").hasRole("ADMIN")
                            .requestMatchers(PUT, "/api/admin/doctors/*").hasRole("ADMIN")
                            .requestMatchers(DELETE, "/api/admin/doctors/*").hasRole("ADMIN")
                            .requestMatchers(POST, "/api/admin/doctor").hasRole("ADMIN")
                            .requestMatchers(GET, "/api/admin/bookings", "/api/admin/bookings/*").hasRole("ADMIN")
                            .requestMatchers(GET , "/api/admin/specializations").hasAnyRole("ADMIN","DOCTOR", "USER")
                            .requestMatchers(POST, "/api/admin/specialization").hasRole("ADMIN")
                            .requestMatchers(GET,"/api/admin/bookings", "/api/admin/bookings/*").hasRole("ADMIN")
                            .requestMatchers(PUT, "/api/admin/bookings/*").hasRole("ADMIN")
                            .requestMatchers(DELETE, "/api/admin/specialization/*").hasRole("ADMIN")
                            .requestMatchers(POST, "/api/users/booking").hasAnyRole("ADMIN", "USER", "DOCTOR")
                            .requestMatchers(GET, "/api/users/bookings").hasRole("USER")
                            .requestMatchers(DELETE, "/api/users/bookings/*").hasRole("USER")
                            .requestMatchers(GET, "/api/users/profile").hasRole("USER")
                            .requestMatchers(GET,"/api/doctor", "/api/doctor/*").hasAnyRole("DOCTOR","USER", "ADMIN")
                            .requestMatchers(GET, "/api/doctor/*").hasRole("DOCTOR")
                            .requestMatchers(PUT, "/api/doctor/bookings-status/*").hasRole("DOCTOR")
                            .requestMatchers(PUT, "/api/doctor/*").hasRole("DOCTOR")
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}
