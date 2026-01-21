package com.BTL_JAVA.BTL.DTO.Request.Sales;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesCreationRequest {
    @NotBlank(message = "Tên sale không được để trống")
    String name;
    
    String description;
    
    @NotNull(message = "Ngày bắt đầu không được để trống")
    LocalDateTime stDate;
    
    @NotNull(message = "Ngày kết thúc không được để trống")
    LocalDateTime endDate;
}