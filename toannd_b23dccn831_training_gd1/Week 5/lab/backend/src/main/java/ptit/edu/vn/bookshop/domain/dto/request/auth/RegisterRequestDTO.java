package ptit.edu.vn.bookshop.domain.dto.request.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.Set;

@Getter
public class RegisterRequestDTO {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ\\s']+$",
            message = "Full name can only contain letters, spaces, and apostrophes"
    )
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+?84|0)[35789]\\d{8}$",
            message = "Invalid Vietnamese phone number format"
    )
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._-]).{8,64}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    @Size(min = 8, max = 64, message = "Confirm password must be between 8 and 64 characters")
    private String confirmPassword;

    // Custom validation for password confirmation
    @AssertTrue(message = "Password and confirm password must match")
    public boolean isPasswordMatching() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }

    // Prevent common passwords
    @AssertFalse(message = "Password is too common or weak")
    public boolean isCommonPassword() {
        if (password == null) return false;

        Set<String> commonPasswords = Set.of(
                "password", "12345678", "qwerty", "admin", "welcome",
                "123456789", "11111111", "password1", "abc123"
        );
        return commonPasswords.contains(password.toLowerCase());
    }
}
