package com.fintrack.backend.budget.service;

import com.fintrack.backend.budget.model.Budget;
import com.fintrack.backend.budget.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    // 1. CẬP NHẬT: Lấy danh sách ngân sách (Hỗ trợ lọc theo tháng)
    public List<Budget> getBudgets(Long userId, String month) {
        if (month != null && !month.isEmpty()) {
            // Nếu có truyền tháng lên -> Lọc đúng tháng đó
            return budgetRepository.findByUserIdAndMonth(userId, month);
        }
        // Nếu không truyền tháng -> Lấy toàn bộ ngân sách của User đó
        // (Lưu ý: Bạn cần đảm bảo Repository đã có hàm findByUserId nhé)
        return budgetRepository.findByUserId(userId);
    }

    // 2. THÊM MỚI: Lấy danh sách tên danh mục duy nhất (DISTINCT)
    // Dùng để hiển thị vào Dropdown chọn danh mục mà không bị trùng lặp
    public List<String> getUniqueCategories(Long userId) {
        return budgetRepository.findDistinctCategoriesByUserId(userId);
    }

    // --- CÁC HÀM DƯỚI GIỮ NGUYÊN ---

    public List<Budget> getAllBudgets() {
        // Hàm này nên hạn chế dùng hoặc xóa đi vì nó lấy data của cả hệ thống
        return budgetRepository.findAll();
    }

    @Transactional
    public Budget createBudget(Budget budget) {
        if (budget.getSpent() == null) budget.setSpent(0.0);
        if (budget.getUserId() == null) budget.setUserId(1L);
        return budgetRepository.save(budget);
    }

    @Transactional
    public Budget updateBudget(Long id, Budget updated) {
        Budget existing = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ngân sách"));

        // Chỉ cho sửa thông tin kế hoạch
        existing.setCategory(updated.getCategory());
        existing.setLimitAmount(updated.getLimitAmount());
        existing.setMonth(updated.getMonth());

        // Không cho sửa tay 'spent' (vì nó phải tự động tính từ Transaction)
        return budgetRepository.save(existing);
    }

    @Transactional
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
