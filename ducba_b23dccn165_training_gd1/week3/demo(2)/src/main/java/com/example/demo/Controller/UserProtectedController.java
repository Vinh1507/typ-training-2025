package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserProtectedController {

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User profile");
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("access", "User và Admin có thể truy cập");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, String>> userDashboard() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to User Dashboard");
        response.put("access", "User and Admin có thể truy cập");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, String>> getUserData() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User data");
        response.put("note", "User and Admin đều có thể truy cập");
        return ResponseEntity.ok(response);
    }
}
