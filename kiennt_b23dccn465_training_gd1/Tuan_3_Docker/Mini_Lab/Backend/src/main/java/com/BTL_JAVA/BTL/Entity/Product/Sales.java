package com.BTL_JAVA.BTL.Entity.Product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@Entity
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "name", length = 100, nullable = false)
    String name;
    @Column(name = "description", length = 300, nullable = false)
    String description;
    @Column(name = "st_date", nullable = false)
    LocalDateTime stDate;

    @Column(name = "end_date", nullable = false)
    LocalDateTime endDate;

    @Column(name = "active", nullable = false)
    boolean active = true;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductSale> productSales = new ArrayList<>();
}