package ptit.edu.vn.bookshop.domain.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerificationRequestDTO {
    @NotBlank(message = "OTP code is required")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "OTP must contain only digits"
    )
    private String otp;
}
