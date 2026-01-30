package ptit.edu.vn.bookshop.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.DiscountTypeEnum;
import ptit.edu.vn.bookshop.domain.constant.OrderStatusEnum;
import ptit.edu.vn.bookshop.domain.entity.OrderItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private OrderStatusEnum status;
    private ShippingInfo shippingInfo;
    private String paymentMethod;
    private List<OrderItemResponse> items;
    private Summary summary;
    private Instant createdAt;
    private Instant updateAt;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShippingInfo {
        private String receiverName;
        private String receiverPhone;
        private String receiverAddress;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private String imageUrl;
        private BigDecimal unitPrice;
        private BigDecimal discount;
        private BigDecimal discountedPrice;
        private BigDecimal totalPrice;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Summary {
        private BigDecimal subtotal;
        private BigDecimal shippingFee;
        private BigDecimal discountFee;
        private BigDecimal finalPrice;
    }
}
