// Đảm bảo dòng package khớp với của bạn
package com.fintrack.backend.auth.controllers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @Email
    @NotBlank
    private String email;
}