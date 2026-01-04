package com.fintrack.backend.transaction.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Map vào cột 'transaction_date' trong SQL cho chuẩn nghĩa
    // (Dù SQL bạn có cả cột 'date', nhưng dùng 'transaction_date' an toàn hơn)
    @Column(name = "transaction_date", nullable = false)
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "is_income")
    @JsonProperty("isIncome")
    private boolean isIncome;

    // Vẫn giữ cột này để lưu tên danh mục (cho hiển thị đơn giản)
    @Column(name = "category")
    private String category;

    // --- THÊM MỚI: Map vào khóa ngoại category_id ---
    // Để sau này bạn làm tính năng thống kê theo danh mục
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "wallet_id")
    private Long walletId;
}
