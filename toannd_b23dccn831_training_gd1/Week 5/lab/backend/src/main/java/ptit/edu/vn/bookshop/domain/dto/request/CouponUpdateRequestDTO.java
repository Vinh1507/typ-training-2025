package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import ptit.edu.vn.bookshop.domain.constant.DiscountTypeEnum;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CouponUpdateRequestDTO {
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private DiscountTypeEnum discountType;
    private BigDecimal discountValue;
    private BigDecimal minimumOrderAmount;
    private BigDecimal maximumDiscountAmount;
    private BigDecimal usageLimit;
    private BigDecimal usageLimitPerCustomer;
    private LocalDateTime startsAt;
    private LocalDateTime expiresAt;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
