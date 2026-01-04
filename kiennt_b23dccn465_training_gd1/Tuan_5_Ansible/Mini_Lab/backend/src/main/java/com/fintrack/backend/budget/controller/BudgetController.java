package com.fintrack.backend.budget.controller;

import com.fintrack.backend.budget.model.Budget;
import com.fintrack.backend.budget.service.BudgetService;
import com.fintrack.backend.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // 1. Lấy danh sách Ngân sách (Có hỗ trợ lọc theo tháng ?month=2025-11)
    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets(
            @AuthenticationPrincipal User user, // Tự động lấy User từ Token
            @RequestParam(required = false) String month // Tham số tháng (tùy chọn)
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(budgetService.getBudgets(user.getId(), month));
    }

    // 2. API MỚI: Lấy danh sách tên danh mục (Cho Dropdown không trùng lặp)
    @GetMapping("/categories-list")
    public ResponseEntity<List<String>> getUniqueCategories(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(budgetService.getUniqueCategories(user.getId()));
    }

    // 3. Tạo ngân sách mới
    @PostMapping
    public ResponseEntity<Budget> createBudget(
            @AuthenticationPrincipal User user,
            @RequestBody Budget budget
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }
        budget.setUserId(user.getId()); // Tự động gán ngân sách cho người đang đăng nhập
        return ResponseEntity.ok(budgetService.createBudget(budget));
    }

    // 4. Cập nhật ngân sách
    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.updateBudget(id, budget));
    }

    // 5. Xóa ngân sách
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
