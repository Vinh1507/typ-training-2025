package ptit.edu.vn.bookshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.DiscountTypeEnum;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountTypeEnum discountType;

    @Column(name = "discount_value", nullable = false, precision = 15, scale = 0)
    private BigDecimal discountValue;

    // Điều kiện áp dụng
    @Column(name = "minimum_order_amount", precision = 15, scale = 0)
    private BigDecimal minimumOrderAmount;

    @Column(name = "maximum_discount_amount", precision = 15, scale = 0)
    private BigDecimal maximumDiscountAmount;

    // Giới hạn sử dụng
    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usage_limit_per_customer")
    private Integer usageLimitPerCustomer;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @ManyToMany(mappedBy = "coupons", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        if (this.status == null) {
            this.status = StatusEnum.ACTIVE;
        }
        if (this.usageLimit == null) this.usageLimit = 0;
        if (this.maximumDiscountAmount == null) this.minimumOrderAmount = BigDecimal.ZERO;
        if (this.minimumOrderAmount == null) this.maximumDiscountAmount = BigDecimal.ZERO;

    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }


}
