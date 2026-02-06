package com.BTL_JAVA.BTL.DTO.Request.Product;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {
    @Min(value = 1, message = "Product variation ID phải lớn hơn 0")
    int product_variation_id;
    
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    int quantity;
}