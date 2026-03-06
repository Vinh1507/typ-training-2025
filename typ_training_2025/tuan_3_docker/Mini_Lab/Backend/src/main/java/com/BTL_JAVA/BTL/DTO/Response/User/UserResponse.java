package com.BTL_JAVA.BTL.DTO.Response.User;


import com.BTL_JAVA.BTL.DTO.Response.Security.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
     int id;

     String userName;

     String email;

     String phoneNumber;

     String avatar;

     Set<RoleResponse> roles;
}

