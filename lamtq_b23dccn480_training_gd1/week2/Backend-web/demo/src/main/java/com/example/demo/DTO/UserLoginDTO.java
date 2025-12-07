package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @JsonProperty("username")
    @NotBlank(message = "Username is required")
    private String userName;

    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    private String password;
}
