package com.example.demo.Converter;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ConvertToDTO {
    public ProductDTO toDTO(ProductEntity entity) {
        ProductDTO dto = new ProductDTO();

        dto.setId(entity.getId());
        dto.setProductName(entity.getProductName());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());
        dto.setDescription(entity.getDescription());

        return dto;
    }

}
