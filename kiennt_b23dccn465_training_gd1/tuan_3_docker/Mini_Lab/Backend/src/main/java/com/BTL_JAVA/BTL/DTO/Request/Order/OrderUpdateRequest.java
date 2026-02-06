package com.BTL_JAVA.BTL.DTO.Request.Order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    Integer addressId;
    String phoneNumber;
    String note;
}

