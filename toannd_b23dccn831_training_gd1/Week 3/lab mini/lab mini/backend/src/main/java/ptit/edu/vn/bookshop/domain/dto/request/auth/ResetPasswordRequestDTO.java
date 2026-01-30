package ptit.edu.vn.bookshop.domain.dto.request.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ResetPasswordRequestDTO {
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._-]).{8,64}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String newPassword;

    @NotBlank(message = "Confirm new password is required")
    @Size(min = 8, max = 64, message = "Confirm password must be between 8 and 64 characters")
    private String confirmNewPassword;

    @NotBlank(message = "Reset token is required")
    @Size(min = 32, max = 255, message = "Reset token must be between 32 and 255 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9-_]+$",
            message = "Reset token contains invalid characters"
    )
    private String resetToken;

    @AssertTrue(message = "New password and confirm password must match")
    public boolean isPasswordMatching() {
        if (newPassword == null || confirmNewPassword == null) {
            return false;
        }
        return newPassword.equals(confirmNewPassword);
    }
}
