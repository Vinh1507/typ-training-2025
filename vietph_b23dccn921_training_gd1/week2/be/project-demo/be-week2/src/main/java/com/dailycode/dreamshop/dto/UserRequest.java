package com.dailycode.dreamshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
     private String username;
    private String password;
    @NotBlank(message = "Fullname is required!")
    private String fullname;
}
