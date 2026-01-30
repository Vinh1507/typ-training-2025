package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddCartItemRequestDTO {

    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be a positive number")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}
