package ptit.edu.vn.bookshop.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.BookStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private List<CartItemResponseDTO> cartItems;
    private CartSummaryDTO summary;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemResponseDTO{
        private Long id;
        private Long productId;
        private String productName;
        @Enumerated(EnumType.STRING)
        private BookStatusEnum productStatus;
        private String imageUrl;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal discount;
        private BigDecimal discountedPrice;
        private BigDecimal finalPrice;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartSummaryDTO {
        private Integer totalQuantity;
        private BigDecimal subtotal;
    }

}
