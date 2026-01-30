package ptit.edu.vn.bookshop.domain.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import ptit.edu.vn.bookshop.domain.constant.OrderStatusEnum;

@Getter
public class UpdateStatusRequestDTO {
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;
}
