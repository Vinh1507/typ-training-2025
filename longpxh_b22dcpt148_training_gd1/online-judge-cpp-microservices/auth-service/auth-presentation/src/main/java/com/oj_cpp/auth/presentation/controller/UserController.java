package com.oj_cpp.auth.presentation.controller;

import com.oj_cpp.auth.application.dto.response.UserResponse;
import com.oj_cpp.auth.domain.model.Role;
import com.oj_cpp.auth.domain.model.User;
import com.oj_cpp.auth.domain.service.RoleService;
import com.oj_cpp.auth.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/my-info")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal Jwt jwt) {
    
        String username = jwt.getSubject();
        
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        var roles = roleService.findByUserId(user.getId())
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return ResponseEntity.ok(UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build());
    }
}
