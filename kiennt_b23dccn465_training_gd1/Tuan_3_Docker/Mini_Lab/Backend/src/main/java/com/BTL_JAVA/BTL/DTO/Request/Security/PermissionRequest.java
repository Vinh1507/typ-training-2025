package com.BTL_JAVA.BTL.DTO.Request.Security;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {
    String namePermission;
    String description;
}
