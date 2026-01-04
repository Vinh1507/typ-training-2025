package com.fintrack.backend.setting.controller;

import com.fintrack.backend.auth.models.User;
import com.fintrack.backend.auth.repositories.UserRepository;
import com.fintrack.backend.setting.model.Category;
import com.fintrack.backend.setting.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BackupController {
    private final UserRepository userRepo;
    private final CategoryRepository catRepo;

    @GetMapping("/backup")
    public ResponseEntity<?> backup() {
        try {
            List<User> users = userRepo.findAll();
            List<Category> cats = catRepo.findAll();

            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("categories", cats);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to backup data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/restore")
    @Transactional
    public ResponseEntity<?> restore(@RequestBody Map<String, Object> body) {
        try {
            userRepo.deleteAll();
            catRepo.deleteAll();

            // Khôi phục categories
            Object catsObj = body.get("categories");
            if (catsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> list = (List<Map<String, Object>>) catsObj;
                for (Map<String, Object> m : list) {
                    Object name = m.get("name");
                    if (name != null) catRepo.save(new Category(name.toString()));
                }
            }

            // Khôi phục users
            Object usersObj = body.get("users");
            if (usersObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> list = (List<Map<String, Object>>) usersObj;
                for (Map<String, Object> m : list) {
                    String fullName = (String) m.get("fullName");
                    String email = (String) m.get("email");
                    String phone = (String) m.get("phone");
                    String password = (String) m.get("password");
                    if (email != null) {
                        User user = new User();
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setFullName(fullName);
                        user.setPhone(phone);
                        userRepo.save(user);
                    }
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Data restored successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to restore data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}