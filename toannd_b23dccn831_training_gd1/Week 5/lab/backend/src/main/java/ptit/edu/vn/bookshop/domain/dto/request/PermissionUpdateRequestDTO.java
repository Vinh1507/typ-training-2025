package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

@Getter
public class PermissionUpdateRequestDTO {
    @NotBlank(message = "Permission name is required")
    @Size(min = 2, max = 50, message = "Permission name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z_][A-Za-z0-9_.:\\s-]*$",
            message = "Permission name can only contain letters, numbers, underscores, dots, colons, and hyphens"
    )
    private String name;

    @NotBlank(message = "Module name is required")
    @Size(max = 50, message = "Module name must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9_\\-.]*$",
            message = "Module name can only contain letters, numbers, underscores, dots, and hyphens"
    )
    private String module;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
