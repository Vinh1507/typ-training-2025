package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, String>> adminDashboard() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Admin Dashboard");
        response.put("access", "Chỉ admin mới truy cập được");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, String>> getAllUsers() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Danh sách users");
        response.put("note", "Chỉ admin mới truy cập được");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/settings")
    public ResponseEntity<Map<String, String>> adminSettings() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin settings");
        response.put("access", "Admin cài đặt");
        return ResponseEntity.ok(response);
    }
}
