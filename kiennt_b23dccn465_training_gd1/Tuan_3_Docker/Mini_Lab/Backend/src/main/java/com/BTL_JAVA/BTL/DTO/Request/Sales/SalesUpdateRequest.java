package com.BTL_JAVA.BTL.DTO.Request.Sales;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesUpdateRequest {
    String name;
    String description;
    LocalDateTime stDate;
    LocalDateTime endDate;
    List<ProductSaleItemRequest> addProducts;
    List<Integer> removeProductIds;
}

