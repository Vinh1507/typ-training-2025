package com.example.demo.config;

import com.example.demo.model.Account;
import com.example.demo.model.AccRole;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.AccRoleRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccRoleRepository accRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");

        // Create admin account if not exists
        if (!accountRepository.existsByUsername("admin")) {
            Account adminAccount = new Account();
            adminAccount.setUsername("admin");
            adminAccount.setPassword(passwordEncoder.encode("admin123"));
            adminAccount.setEnabled(true);
            adminAccount = accountRepository.save(adminAccount);

            AccRole adminRole = new AccRole();
            adminRole.setAccount(adminAccount);
            adminRole.setRole(Role.ADMIN);
            accRoleRepository.save(adminRole);

            log.info("Admin account created successfully");
        } else {
            log.info("Admin account already exists");
        }

        // Create 4 user accounts if not exist
        createUserIfNotExists("user1", "user1@example.com", "User One", "0123456789", "Address 1");
        createUserIfNotExists("user2", "user2@example.com", "User Two", "0123456790", "Address 2");
        createUserIfNotExists("user3", "user3@example.com", "User Three", "0123456791", "Address 3");
        createUserIfNotExists("user4", "user4@example.com", "User Four", "0123456792", "Address 4");

        log.info("Data initialization completed");
    }

    private void createUserIfNotExists(String username, String email, String name, String phone, String address) {
        if (!accountRepository.existsByUsername(username)) {
            // Create account
            Account userAccount = new Account();
            userAccount.setUsername(username);
            userAccount.setPassword(passwordEncoder.encode("password123"));
            userAccount.setEnabled(true);
            userAccount = accountRepository.save(userAccount);

            // Create role
            AccRole userRole = new AccRole();
            userRole.setAccount(userAccount);
            userRole.setRole(Role.USER);
            accRoleRepository.save(userRole);

            // Create user info
            if (!userRepository.existsByEmail(email)) {
                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);
                userRepository.save(user);

                log.info("User account created: {}", username);
            }
        } else {
            log.info("User account already exists: {}", username);
        }
    }
}
