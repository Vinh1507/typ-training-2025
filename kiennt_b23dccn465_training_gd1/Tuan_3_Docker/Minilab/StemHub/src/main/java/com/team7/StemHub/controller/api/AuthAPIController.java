package com.team7.StemHub.controller.api;

import com.team7.StemHub.dto.request.SignupRequest;
import com.team7.StemHub.model.User;
import com.team7.StemHub.model.enums.Role;
import com.team7.StemHub.service.AuthService;
import com.team7.StemHub.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthAPIController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest request) {
        // Check if username already exists
        if (authService.findByUsername(request.getUsername()) != null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Tên đăng nhập đã tồn tại");
            return ResponseEntity.badRequest().body(error);
        }

        // Create user from DTO
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .fullname(request.getFullname())
                .role(Role.USER)
                .build();

        User registered = authService.register(user);

        // Return success response without sensitive data
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng ký thành công");
        response.put("username", registered.getUsername());
        response.put("email", registered.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // load UserDetails để tạo token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        // trả về JSON chứa token
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        User user = authService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).build();
        }
        // Return only minimal info to avoid serializing entire entity graph
        Map<String, Object> body = new HashMap<>();
        body.put("userId", user.getId());
        body.put("username", user.getUsername());
        body.put("fullname", user.getFullname());
        return ResponseEntity.ok(body);
    }
}