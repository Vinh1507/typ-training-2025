package com.BTL_JAVA.BTL.DTO.Request.Product;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariationCreationRequest {
    Integer productId;
    String size;
    String color;
    Integer stockQuantity;
    MultipartFile image;
}
