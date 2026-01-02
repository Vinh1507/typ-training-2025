package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
@Getter @Setter
public class Product {
    @Id
    private Long id;

    private String name;
    private BigDecimal price;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
