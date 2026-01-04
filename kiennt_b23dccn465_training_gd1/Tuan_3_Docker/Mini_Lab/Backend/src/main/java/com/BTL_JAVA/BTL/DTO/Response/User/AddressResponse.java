package com.BTL_JAVA.BTL.DTO.Response.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    int address_id;
    String street;
    String ward;
    String city;
    boolean is_default;
}
