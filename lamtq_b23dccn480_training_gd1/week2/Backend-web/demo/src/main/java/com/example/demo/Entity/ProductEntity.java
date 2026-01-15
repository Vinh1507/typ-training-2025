package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private long quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
