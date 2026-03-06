package com.BTL_JAVA.BTL.DTO.Response.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSaleItemResponse {
    Integer id;
    BigDecimal value;
    String image;
}
