package com.BTL_JAVA.BTL.DTO.Request.Sales;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSaleItemRequest {
    @NotNull(message = "Product ID không được để trống")
    Integer productId;
    
    @NotNull(message = "Value không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Value phải lớn hơn 0")
    BigDecimal value;
}
