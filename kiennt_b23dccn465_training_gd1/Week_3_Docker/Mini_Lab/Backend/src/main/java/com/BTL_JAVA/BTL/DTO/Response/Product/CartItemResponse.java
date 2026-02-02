package com.BTL_JAVA.BTL.DTO.Response.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    int product_id;
    int product_variation_id;
    int quantity;
}
