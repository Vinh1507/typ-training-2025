package com.oj_cpp.auth.presentation.controller;

import com.oj_cpp.auth.application.dto.request.LoginRequest;
import com.oj_cpp.auth.application.dto.request.RegisterRequest;
import com.oj_cpp.auth.application.dto.response.AuthResponse;
import com.oj_cpp.auth.application.dto.response.UserResponse;
import com.oj_cpp.auth.application.service.AuthService;
import com.oj_cpp.auth.application.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody com.oj_cpp.auth.application.dto.request.RefreshTokenRequest request) {
        AuthResponse response = loginService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
