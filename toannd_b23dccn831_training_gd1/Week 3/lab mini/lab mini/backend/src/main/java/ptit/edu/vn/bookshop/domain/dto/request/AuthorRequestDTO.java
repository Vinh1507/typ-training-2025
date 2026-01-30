package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.GenderEnum;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

import java.time.LocalDate;

@Getter
public class AuthorRequestDTO {

    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ\\s'.-]+$",
            message = "Author name can only contain letters, spaces, apostrophes, dots and hyphens"
    )
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ\\s-]+$",
            message = "Country can only contain letters, spaces and hyphens"
    )
    private String country;

    @Size(max = 2000, message = "Biography must not exceed 2000 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ0-9\\s.,!?():;'\"\\-\\n\\r]*$",
            message = "Biography contains invalid characters"
    )
    private String biography;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}