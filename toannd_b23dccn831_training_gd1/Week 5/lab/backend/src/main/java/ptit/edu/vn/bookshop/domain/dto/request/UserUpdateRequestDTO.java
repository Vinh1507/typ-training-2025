package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ptit.edu.vn.bookshop.domain.constant.GenderEnum;
import lombok.Getter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

import java.time.LocalDate;

@Getter
public class UserUpdateRequestDTO {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ'\\-\\s]+$", message = "Full name can only contain letters, spaces, apostrophes and hyphens")
    private String name;

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

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Size(max = 255, message = "Avatar URL must not exceed 255 characters")
    @Pattern(
            regexp = "^(|https?://.*\\.(?:png|jpg|jpeg|gif|svg|webp))?$",
            message = "Avatar must be empty or a valid image URL"
    )
    private String avatar;
}
