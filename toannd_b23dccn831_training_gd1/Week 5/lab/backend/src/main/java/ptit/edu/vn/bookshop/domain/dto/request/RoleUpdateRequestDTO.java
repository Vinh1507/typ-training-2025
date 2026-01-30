package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

import java.util.List;

@Getter
public class RoleUpdateRequestDTO {
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ0-9\\s.,!?@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|<>/~`]*$",
            message = "Description contains invalid characters"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @NotNull(message = "Permissions list is required")
    @Size(min = 1, message = "At least one permission is required")
    @Valid
    private List<RoleCreateRequestDTO.RolePermissionRequestDTO> permissions;

    @Getter
    @Setter
    public static class RolePermissionRequestDTO {

        @NotNull(message = "Permission ID is required")
        @Min(value = 1, message = "Permission ID must be a positive number")
        private Long id;
    }
}
