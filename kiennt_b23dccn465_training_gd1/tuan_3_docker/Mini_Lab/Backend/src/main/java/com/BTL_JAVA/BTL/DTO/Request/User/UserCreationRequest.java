package com.BTL_JAVA.BTL.DTO.Request.User;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 2,message = "USERNAME_INVALID")
    private String username;

    private String email;

    @Size(min = 3,message = "INVALID_PASSWORD")
    private String password;

    private String phoneNumber;

    private MultipartFile avatar;

}
