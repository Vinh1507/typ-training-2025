package com.fintrack.backend.auth.controllers;

// ... import các DTOs ...
import com.fintrack.backend.auth.controllers.dtos.*;
import com.fintrack.backend.auth.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    // API QUÊN MẬT KHẨU
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        authService.processForgotPassword(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Nếu email của bạn tồn tại, một hướng" +
                " dẫn sẽ được gửi đến."));
    }

    // API ĐẶT LẠI MẬT KHẨU
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        authService.processResetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Mật khẩu đã được đặt lại thành công."));
    }
}