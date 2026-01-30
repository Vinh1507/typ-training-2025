package ptit.edu.vn.bookshop.domain.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.DiscountTypeEnum;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponseDTO {
    private String id;
    private String code;
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
    private Instant  createdAt;
    private Instant updatedAt;

}
