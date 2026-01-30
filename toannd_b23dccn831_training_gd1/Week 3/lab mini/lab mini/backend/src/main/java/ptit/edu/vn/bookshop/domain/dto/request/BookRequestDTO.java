package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.BookStatusEnum;

import java.math.BigDecimal;

@Getter
public class BookRequestDTO {

    @NotBlank(message = "Book name is required")
    @Size(min = 1, max = 255, message = "Book name must be between 1 and 255 characters")
    private String name;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotBlank(message = "Language is required")
    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 10000, message = "Quantity cannot exceed 10000")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount cannot exceed 100%")
    private BigDecimal discount;

    @Enumerated(EnumType.STRING)
    private BookStatusEnum status;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(|https?://.*\\.(?:png|jpg|jpeg|gif|webp|svg))$",
            message = "Image must be a valid image URL or empty"
    )
    private String imageUrl;

    @NotNull(message = "Category is required")
    @Valid
    private BookCategoryRequestDTO category;

    @NotNull(message = "Author is required")
    @Valid
    private BookAuthorRequestDTO author;

    @NotNull(message = "Publisher is required")
    @Valid
    private BookPublisherRequestDTO publisher;

    @Getter
    @Setter
    public static class BookCategoryRequestDTO {
        @NotNull(message = "Category ID is required")
        @Min(value = 1, message = "Category ID must be a positive number")
        private Long id;
    }

    @Getter
    @Setter
    public static class BookAuthorRequestDTO {
        @NotNull(message = "Author ID is required")
        @Min(value = 1, message = "Author ID must be a positive number")
        private Long id;
    }

    @Getter
    @Setter
    public static class BookPublisherRequestDTO {
        @NotNull(message = "Publisher ID is required")
        @Min(value = 1, message = "Publisher ID must be a positive number")
        private Long id;
    }
}