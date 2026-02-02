package com.BTL_JAVA.BTL.DTO.Request.User;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {
    @NotBlank(message = "Địa chỉ không được để trống")
    String street;
    
    @NotBlank(message = "Phường/Xã không được để trống")
    String ward;
    
    @NotBlank(message = "Thành phố không được để trống")
    String city;
    
    boolean defaultAddress;
}