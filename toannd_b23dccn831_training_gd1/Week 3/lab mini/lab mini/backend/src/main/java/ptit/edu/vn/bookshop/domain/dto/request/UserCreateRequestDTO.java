package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ptit.edu.vn.bookshop.domain.constant.GenderEnum;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserCreateRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ'\\-\\s]+$", message = "Full name can only contain letters, spaces, apostrophes and hyphens")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp =  "^0\\d{9}$",
            message = "Invalid Vietnamese phone number format"
    )
    private String phone;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Size(max = 255, message = "Avatar URL must not exceed 255 characters")
    @Pattern(
            regexp = "^(|https?://.*\\.(?:png|jpg|jpeg|gif|svg|webp))?$",
            message = "Avatar must be empty or a valid image URL"
    )
    private String avatar;

    @NotNull(message = "Role information is required")
    @Valid
    private UserRoleUpdateRequestDTO role;

    @Getter
    public static class UserRoleUpdateRequestDTO{
        @NotNull(message = "Role ID is required")
        @Min(value = 1, message = "Role ID must be a positive number")
        private Long id;
    }
}
