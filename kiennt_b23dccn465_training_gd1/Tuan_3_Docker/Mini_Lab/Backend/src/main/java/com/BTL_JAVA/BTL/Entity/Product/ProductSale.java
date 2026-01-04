package com.BTL_JAVA.BTL.Entity.Product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "product_sale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    Sales sale;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Column(name = "sale_value", precision = 3, scale = 2)
    BigDecimal saleValue;
}