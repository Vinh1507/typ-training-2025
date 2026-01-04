// Gói (package) ...dtos
package com.fintrack.backend.auth.controllers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    private String phone;

    @NotBlank
    private String password;
}