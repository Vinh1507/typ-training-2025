package ptit.edu.vn.bookshop.domain.dto.request.auth;


import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.Set;

@Getter
public class PasswordChangeRequestDTO {

    @NotBlank(message = "Current password is required")
    @Size(min = 8, max = 64, message = "Current password must be between 8 and 64 characters")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 64, message = "New password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._-]).{8,64}$",
            message = "New password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String newPassword;

    @NotBlank(message = "Confirm new password is required")
    @Size(min = 8, max = 64, message = "Confirm new password must be between 8 and 64 characters")
    private String confirmNewPassword;

    // Custom validation for password confirmation
    @AssertTrue(message = "New password and confirm new password must match")
    public boolean isNewPasswordMatching() {
        if (newPassword == null || confirmNewPassword == null) {
            return false;
        }
        return newPassword.equals(confirmNewPassword);
    }

    // Security: Prevent using the same password as current
    @AssertFalse(message = "New password must be different from current password")
    public boolean isNewPasswordSameAsCurrent() {
        if (currentPassword == null || newPassword == null) {
            return false;
        }
        return currentPassword.equals(newPassword);
    }

    // Security: Prevent common passwords
    @AssertFalse(message = "New password is too common or weak")
    public boolean isNewPasswordCommon() {
        if (newPassword == null) return false;

        Set<String> commonPasswords = Set.of(
                "password", "12345678", "qwerty", "admin", "welcome",
                "123456789", "11111111", "password1", "abc123"
        );
        return commonPasswords.contains(newPassword.toLowerCase());
    }
}