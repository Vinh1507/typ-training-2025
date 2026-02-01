package com.oj_cpp.auth.application.service;

import com.oj_cpp.auth.application.dto.request.RegisterRequest;
import com.oj_cpp.auth.application.dto.response.UserResponse;
import com.oj_cpp.auth.domain.model.Role;
import com.oj_cpp.auth.domain.model.User;
import com.oj_cpp.auth.domain.service.PasswordHasher;
import com.oj_cpp.auth.domain.service.RoleService;
import com.oj_cpp.auth.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordHasher passwordHasher;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String hashedPassword = passwordHasher.make(request.getPassword());

        User newUser = new User(
                null,
                request.getUsername(),
                request.getEmail(),
                request.getFullName(),
                hashedPassword,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        User savedUser = userService.createUser(newUser);

        Role defaultRole = roleService.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        roleService.assignRoleToUser(savedUser.getId(), defaultRole.getId());

        List<String> roles = roleService.findByUserId(savedUser.getId())
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .roles(roles)
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }
}
