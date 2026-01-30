package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDTO {

    @NotBlank(message = "Address name is required")
    @Size(min = 2, max = 100, message = "Address name must be between 2 and 100 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ0-9\\s.,()-]+$",
            message = "Address name can only contain letters, numbers, spaces, and common punctuation"
    )
    private String receiverName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+?84|0)[35789]\\d{8}$",
            message = "Invalid Vietnamese phone number format"
    )
    private String phone;

    @NotBlank(message = "Street address is required")
    @Size(max = 255, message = "Street address must not exceed 255 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ0-9\\s.,/-]+$",
            message = "Street address contains invalid characters"
    )
    private String street;

    @NotBlank(message = "Ward is required")
    @Size(max = 50, message = "Ward must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$",
            message = "Ward can only contain letters, numbers and spaces"
    )
    private String ward;

    @NotBlank(message = "District is required")
    @Size(max = 50, message = "District must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$",
            message = "District can only contain letters, numbers and spaces"
    )
    private String district;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÀ-ỹ\\s]+$",
            message = "City can only contain letters and spaces"
    )
    private String city;

    private Boolean isDefault;
}