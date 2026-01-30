package ptit.edu.vn.bookshop.config;

import org.springframework.http.HttpMethod;
import ptit.edu.vn.bookshop.service.UserService;
import ptit.edu.vn.bookshop.service.impl.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigutation {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userSerive) {
        return new CustomUserDetailService(userSerive);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        // authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    String[] whiteList = {
            "/",
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/verify",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/reset-password",
            "/api/v1/auth/verify-otp",
            "/api/v1/files/**",
            "/storage/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        http
                .csrf(c -> c.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whiteList).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/books").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/coupons").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable())
                .exceptionHandling(
                        exceptions -> exceptions
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


}
