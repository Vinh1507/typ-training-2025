package com.BTL_JAVA.BTL.Entity.Orders;

import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_order_date", columnList = "order_date")
})
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "full_address", nullable = false, length = 500)
    String fullAddress;

    @Column(name = "phone_number", length = 20, nullable = false)
    String phoneNumber;

    @Column(columnDefinition = "TEXT")
    String note;

    @Column(name = "total_amount", nullable = false)
    Double totalAmount;

    @Column(name = "order_date", nullable = false)
    LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<OrderDetail> orderDetails;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }
}
