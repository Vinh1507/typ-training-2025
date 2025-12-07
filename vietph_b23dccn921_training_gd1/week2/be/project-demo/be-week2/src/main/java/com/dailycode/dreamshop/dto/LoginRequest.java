package com.dailycode.dreamshop.dto;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @Email(message = "Email is not valid!")
    @NotBlank(message = "Email is requied!")
    String email;

    @Length(min  = 6,message = "Password must be atleast 6 characters")
    @NotBlank(message = "Password is required!")
    String password;
}
