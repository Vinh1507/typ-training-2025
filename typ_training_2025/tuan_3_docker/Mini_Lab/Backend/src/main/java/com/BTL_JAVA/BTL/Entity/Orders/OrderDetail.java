package com.BTL_JAVA.BTL.Entity.Orders;

import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order_detail", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"order_id", "variation_id"})
    },
    indexes = {
        @Index(name = "idx_order_id", columnList = "order_id"),
        @Index(name = "idx_variation_id", columnList = "variation_id")
    }
)

public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id", nullable = false)
    ProductVariation productVariation;

    @Column(nullable = false)
    Double price;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    int quantity;
}
