package com.example.demo.Service;

import com.example.demo.DTO.AuthResponse;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.RegisterDTO;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterDTO request) {
        // Check nếu đã tồn tại email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Tạo mới user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
        user.setRole(request.getRole() != null && !request.getRole().isEmpty()
                ? request.getRole().toUpperCase()
                : "USER");

        User savedUser = userRepository.save(user);

        // Tạo token
        String accessToken = jwtUtil.generateAccessToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getId(), savedUser.getEmail(),
                savedUser.getRole());

        // Lưu refresh token vào database
        savedUser.setRefreshToken(refreshToken);
        userRepository.save(savedUser);

        // Return response
        return new AuthResponse(accessToken, refreshToken, savedUser.getId(), savedUser.getEmail(),
                savedUser.getRole());
    }

    public AuthResponse login(LoginDTO request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Tạo token
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail(), user.getRole());

        // Lưu refresh token vào database
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        // Return response
        return new AuthResponse(accessToken, refreshToken, user.getId(), user.getEmail(), user.getRole());
    }

    public AuthResponse refreshToken(String refreshToken) {
        // Validate refresh token
        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        Optional<User> userOptional = userRepository.findByEmail(jwtUtil.extractUsername(refreshToken));
        if (userOptional.isEmpty() || !refreshToken.equals(userOptional.get().getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userOptional.get();

        // Tạo mới access token và refresh token
        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail(), user.getRole());

        // Lưu refresh token vào database
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        // Return response
        return new AuthResponse(newAccessToken, newRefreshToken, user.getId(), user.getEmail(), user.getRole());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
