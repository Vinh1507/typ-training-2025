package com.BTL_JAVA.BTL.DTO.Request.Security;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    private String nameRoles;
    String description;

    Set<String> permissions;
}
