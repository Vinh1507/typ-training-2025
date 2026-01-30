package ptit.edu.vn.bookshop.domain.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
public class ForgotPasswordRequestDTO {
    @NotBlank(message = "Username or email is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    @Pattern(
            regexp = "^(?!.*[\\s])(?!.*[@]{2,})[A-Za-z0-9._%+-@]+$",
            message = "Username can only contain letters, numbers, dots, underscores, and @ symbol. No spaces or consecutive @ symbols allowed."
    )
    private String email;
}
