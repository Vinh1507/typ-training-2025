package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PermissionCreateRequestDTO {
    @NotBlank(message = "Permission name is required")
    @Size(min = 2, max = 50, message = "Permission name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z_][A-Za-z0-9_.:\\s-]*$",
            message = "Permission name can only contain letters, numbers, underscores, dots, colons, and hyphens"
    )
    private String name;

    @NotBlank(message = "API path is required")
    @Size(max = 255, message = "API path must not exceed 255 characters")
    @Pattern(
            regexp = "^[/a-zA-Z0-9{}\\-_~.!$&'()*+,;=:@%]+$",
            message = "API path contains invalid characters"
    )
    private String apiPath;

    @NotBlank(message = "HTTP method is required")
    @Pattern(
            regexp = "^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)$",
            message = "HTTP method must be one of: GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS"
    )
    private String method;

    @NotBlank(message = "Module name is required")
    @Size(max = 50, message = "Module name must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9_\\-.]*$",
            message = "Module name can only contain letters, numbers, underscores, dots, and hyphens"
    )
    private String module;
}
