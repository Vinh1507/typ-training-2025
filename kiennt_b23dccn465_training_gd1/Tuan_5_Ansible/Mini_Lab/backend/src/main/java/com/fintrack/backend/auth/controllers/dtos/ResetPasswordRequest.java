// Đảm bảo dòng package khớp với của bạn
package com.fintrack.backend.auth.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}