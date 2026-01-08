package com.fintrack.backend.setting.controller;

import com.fintrack.backend.setting.model.Category;
import com.fintrack.backend.setting.repository.CategoryRepository;
import com.fintrack.backend.auth.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3002", "http://localhost:5173", "http://192.168.123.83:81"})
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<?> getAllCategories(@AuthenticationPrincipal User user) {
        try {
            if (user == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Unauthorized");
                return ResponseEntity.status(401).body(errorResponse);
            }
            List<Category> categories = categoryRepository.findByUser_Id(user.getId());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch categories: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> body
    ) {
        try {
            if (user == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Unauthorized");
                return ResponseEntity.status(401).body(errorResponse);
            }

            String name = body.get("name");
            if (name == null || name.trim().isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Category name is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Category category = new Category(name.trim());
            category.setUser(user); // Gán user cho category
            Category savedCategory = categoryRepository.save(category);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("category", savedCategory);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create category: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Category not found");
                return ResponseEntity.notFound().build();
            }

            categoryRepository.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category deleted successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to delete category: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}