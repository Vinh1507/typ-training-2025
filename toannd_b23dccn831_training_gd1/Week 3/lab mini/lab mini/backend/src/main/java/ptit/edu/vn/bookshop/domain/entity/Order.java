package ptit.edu.vn.bookshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ptit.edu.vn.bookshop.domain.constant.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receiver_name", nullable = false, length = 100)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 15)
    private String receiverPhone;

    @Column(name = "street", nullable = false, length = 255)
    private String street;

    @Column(name = "ward", length = 100)
    private String ward;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "total_price", precision = 15, scale = 0,  nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "discount_total", precision = 15, scale = 0,  nullable = false)
    private BigDecimal discountFee;

    @Column(name = "shipping_fee", precision = 15, scale = 0,  nullable = false)
    private BigDecimal shippingFee;

    @Column(name = "final_price", precision = 15, scale = 0,  nullable = false)
    private BigDecimal finalPrice;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatusEnum status;

    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "notes", columnDefinition = "MEDIUMTEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"orders"})
    @JoinTable(name = "order_coupon", joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Coupon> coupons;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        if (this.status == null) {
            this.status = OrderStatusEnum.PENDING;
        }
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }
}
