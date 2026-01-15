package com.example.demo.Converter;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class UpdateEntityFromDTO {
    public void updateProductEntity(ProductDTO productDTO, ProductEntity productEntity) {
        Field[] fields = ProductDTO.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (fieldName.equals("isDeleted") || fieldName.equals("id")) continue;
            try {
                Object fieldValue = field.get(productDTO);
                if (fieldValue != null) {
                    Field entityField = ProductEntity.class.getDeclaredField(fieldName);
                    entityField.setAccessible(true);
                    entityField.set(productEntity, fieldValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

