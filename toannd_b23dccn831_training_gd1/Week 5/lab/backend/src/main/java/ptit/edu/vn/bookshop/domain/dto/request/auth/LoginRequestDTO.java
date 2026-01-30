package ptit.edu.vn.bookshop.domain.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotBlank(message = "Username or email is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    @Pattern(
            regexp = "^(?!.*[\\s])(?!.*[@]{2,})[A-Za-z0-9._%+-@]+$"
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;
}
