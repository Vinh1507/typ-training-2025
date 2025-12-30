package com.example.demo.Configuration;

import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// chay ngam
public class SecurityConfig {
    @Autowired
    private UserRepository userRepository;

    // detail object user
    @Bean
    public UserDetailsService userDetailsService() {
        return userName -> userRepository.findByUserName(userName)
                .orElseThrow(()->new UsernameNotFoundException("Cannot find user with username: " + userName));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // kiem tra thong tin dang nhap,gan UsernamePasswordAuthenticationToken de xac thuc
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider; // Pass Provider trả về Authentication (đã authenticated) => dang nhap thanh cong
    }

    // dieu phoi dang nhap
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager(); // lay authenticationManager tu service de xu ly
    }
}

