package com.bookingcare.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @JsonProperty("email")
    @NotBlank(message = "Username is required")
    private String userName;

    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    private String password;
}
