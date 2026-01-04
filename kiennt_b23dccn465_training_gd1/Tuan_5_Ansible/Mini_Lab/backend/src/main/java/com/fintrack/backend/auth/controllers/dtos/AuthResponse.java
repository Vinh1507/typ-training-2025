// Gói (package) ...dtos
package com.fintrack.backend.auth.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String fullName;
    private Long userId;
    private String email;
}