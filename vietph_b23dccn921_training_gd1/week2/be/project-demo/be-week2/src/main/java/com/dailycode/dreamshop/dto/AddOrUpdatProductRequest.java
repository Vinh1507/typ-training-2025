package com.dailycode.dreamshop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrUpdatProductRequest {
     private String description;
    private String brand;
    private String name;
    private BigDecimal price;
    private int quantity;
    private Long categoryId;
}

