package com.BTL_JAVA.BTL.DTO.Request.Product;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateQuantityRequest {
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    int quantity;
}
