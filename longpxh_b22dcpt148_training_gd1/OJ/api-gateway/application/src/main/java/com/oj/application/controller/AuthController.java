package com.oj.application.controller;

import com.oj.platform.components.classmanagement.domain.service.ClassService;
import com.oj.platform.components.iam.domain.dto.AuthResponse;
import com.oj.platform.components.iam.domain.dto.LoginRequest;
import com.oj.platform.components.iam.domain.service.AuthService;
import com.oj.platform.components.iam.domain.service.PasswordHasher;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.UserJpa;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.oj.platform.core.http.ApiResponse;
import com.oj.platform.core.http.HttpStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PasswordHasher passwordHasher;
    private final ClassService classService;
    private final UserJpaRepository userRepository;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Get user to fetch ID
        UserJpa user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        List<Long> classIds = classService.getStudentClassIds(user.getId());

        AuthResponse response = authService.login(request.getUsername(), request.getPassword(), user.getId(), classIds);
        return ApiResponse.success(response, "Login successful", HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        AuthResponse response = authService.refreshToken(refreshToken);
        return ApiResponse.success(response, "Token refreshed successfully", HttpStatus.OK);
    }

    @PostMapping("/hash-password/{password}")
    public ApiResponse<Map<String, String>> hashPassword(@PathVariable String password) {
        String hashed = passwordHasher.make(password);
        return ApiResponse.success(Map.of("hashed", hashed), "Password hashed successfully", HttpStatus.OK);
    }
}
