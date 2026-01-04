package com.fintrack.backend.budget.model; // Đảm bảo đúng package của bạn

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Tự sinh Getter, Setter, toString...
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Map thẳng vào cột 'category' (VARCHAR) trong DB
    @Column(name = "category")
    private String category;

    @Column(name = "limit_amount")
    private Double limitAmount;

    // Cột này để cộng dồn số tiền đã chi tiêu
    @Column(name = "spent")
    private Double spent;

    @Column(name = "budget_month")
    private String month; // Lưu dạng "2023-11" hoặc tùy format bạn chọn

    // Lưu ID người dùng (Khớp với cột user_id trong SQL)
    @Column(name = "user_id")
    private Long userId;

    // Nếu sau này muốn dùng wallet_id, chỉ cần uncomment dòng dưới:
    // @Column(name = "wallet_id")
    // private Long walletId;
}
