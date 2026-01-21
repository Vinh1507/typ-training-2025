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
public class ProductDetailResponse {
    Integer productId;
    String  title;
    String  description;
    Double  price;
    String  image;
    Integer categoryId;
    Integer variationCount;
    BigDecimal saleValue;
    LocalDateTime createdAt;
    List<ProductVariationGroup> listVariations;
}
