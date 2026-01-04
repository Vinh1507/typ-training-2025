package com.fintrack.backend.budget.repository;

import com.fintrack.backend.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // 1. SỬA LỖI CỦA BẠN: Thêm hàm tìm tất cả ngân sách của User
    List<Budget> findByUserId(Long userId);

    // 2. Tìm theo tháng (Dùng khi lọc trên giao diện)
    List<Budget> findByUserIdAndMonth(Long userId, String month);

    // 3. Tìm đích danh để trừ tiền (Dùng trong TransactionService)
    Optional<Budget> findByUserIdAndMonthAndCategory(Long userId, String month, String category);

    // 4. Lấy danh sách tên danh mục không trùng lặp (Dùng cho Dropdown)
    @Query("SELECT DISTINCT b.category FROM Budget b WHERE b.userId = :userId")
    List<String> findDistinctCategoriesByUserId(Long userId);
}
