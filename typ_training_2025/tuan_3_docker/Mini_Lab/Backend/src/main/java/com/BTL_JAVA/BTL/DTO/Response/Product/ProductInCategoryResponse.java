package com.BTL_JAVA.BTL.DTO.Response.Product;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInCategoryResponse {
        Integer productId;
        String  title;
        Double  price;
        String  image;
        BigDecimal saleValue;
        LocalDateTime createdAt;
        Integer      variationCount;
        List<String> variationImages;
}
