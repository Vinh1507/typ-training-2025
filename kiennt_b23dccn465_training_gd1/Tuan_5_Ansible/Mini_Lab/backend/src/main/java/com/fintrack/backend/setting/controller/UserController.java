package com.fintrack.backend.setting.controller;

import com.fintrack.backend.setting.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserSettingsService userSettingsService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Map<String, Object> profile = userSettingsService.getUserProfile();
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> updateData) {
        try {
            Map<String, Object> result = userSettingsService.updateUserProfile(updateData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordData) {
        try {
            Map<String, Object> result = userSettingsService.changePassword(passwordData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}