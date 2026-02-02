package com.BTL_JAVA.BTL.configuration;

import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import com.BTL_JAVA.BTL.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByFullName("admin").isEmpty()) {
                var roles=new  HashSet<String>();
                roles.add(Role.ADMIN.name());
                User user=User.builder()
                        .fullName("admin")
                        .password(passwordEncoder.encode("admin"))
//                         .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change password");
            }
        };
    }
}
