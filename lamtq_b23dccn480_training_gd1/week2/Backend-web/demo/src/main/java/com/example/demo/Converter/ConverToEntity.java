package com.example.demo.Converter;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ConverToEntity {
    public ProductEntity toEntity(ProductDTO dto) {
        ProductEntity entity = new ProductEntity();
        entity.setId(dto.getId() != null ? dto.getId() : 0);
        entity.setProductName(dto.getProductName());
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
