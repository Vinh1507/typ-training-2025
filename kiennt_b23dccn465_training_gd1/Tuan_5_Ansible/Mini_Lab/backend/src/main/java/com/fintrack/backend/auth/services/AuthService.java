package com.fintrack.backend.auth.services;

// ===================================
// BẮT ĐẦU PHẦN THÊM MỚI (Imports)
// ===================================
import com.fintrack.backend.auth.controllers.dtos.AuthResponse;
import com.fintrack.backend.auth.controllers.dtos.LoginRequest;
import com.fintrack.backend.auth.controllers.dtos.RegisterRequest;
import java.util.Optional;
// ===================================
// KẾT THÚC PHẦN THÊM MỚI
// ===================================

// Imports cũ của bạn
import com.fintrack.backend.auth.models.User;
import com.fintrack.backend.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // <-- Import
import java.util.UUID; // <-- Import

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService; // <-- Thêm mới

    // Logic Đăng ký (với thông báo lỗi thân thiện)
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email này đã được đăng ký. Vui lòng sử" +
                    " dụng email khác.");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .fullName(user.getFullName())
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    // Logic Đăng nhập
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .fullName(user.getFullName())
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    // Logic Quên Mật Khẩu
    public void processForgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

            user.setResetPasswordToken(token);
            user.setResetTokenExpiry(expiryDate);
            userRepository.save(user);

            emailService.sendPasswordResetEmail(user.getEmail(), token);
        }
    }

    // Logic Đặt Lại Mật Khẩu
    public void processResetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token đặt lại mật khẩu không hợp lệ."));
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token đặt lại mật khẩu đã hết hạn.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
}