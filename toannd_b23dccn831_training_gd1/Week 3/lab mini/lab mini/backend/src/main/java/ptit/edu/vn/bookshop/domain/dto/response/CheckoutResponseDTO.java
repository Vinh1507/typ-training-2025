package ptit.edu.vn.bookshop.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.DiscountTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponseDTO {
    private ShippingAddress shippingAddress;
    private List<CheckoutItemDTO> items;
    private CouponInfo couponInfo;
    private String paymentMethods;
    private SummaryCheckout summary;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddress {
        private String name;
        private String phone;
        private String address;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutItemDTO {
        private Long productId;
        private String productName;
        private String imageUrl;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal finalPrice;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponInfo {
        private String code;
        private BigDecimal discountValue;
        private DiscountTypeEnum  discountType;
        private LocalDateTime expiredAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryCheckout {
        private Integer totalQuantity;
        private BigDecimal subtotal;
        private BigDecimal cartDiscount;
        private BigDecimal shippingFee;
        private BigDecimal grandTotal;
    }
}
