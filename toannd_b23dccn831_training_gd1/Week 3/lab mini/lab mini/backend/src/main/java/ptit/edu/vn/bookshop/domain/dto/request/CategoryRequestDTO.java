package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

@Getter
public class CategoryRequestDTO {
    @Null(message = "ID must not be provided for creation")
    @Min(value = 1, message = "ID must be a positive number")
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ0-9\\s&.,()-]+$",
            message = "Category name can only contain letters, numbers, spaces, and common punctuation"
    )
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ0-9\\s.,!?@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|<>/~`]*$",
            message = "Description contains invalid characters"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
