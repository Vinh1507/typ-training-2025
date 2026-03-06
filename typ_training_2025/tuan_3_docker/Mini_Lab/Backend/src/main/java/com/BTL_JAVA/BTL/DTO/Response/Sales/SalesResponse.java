package com.BTL_JAVA.BTL.DTO.Response.Sales;

import com.BTL_JAVA.BTL.DTO.Response.Product.ProductSaleItemResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesResponse {
    Integer id;
    String name;
    String description;
    LocalDateTime stDate;
    LocalDateTime endDate;
    Boolean active;
    List<ProductSaleItemResponse> list_product;
}
