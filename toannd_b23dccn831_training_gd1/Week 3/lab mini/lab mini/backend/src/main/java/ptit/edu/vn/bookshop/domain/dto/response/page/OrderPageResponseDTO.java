package ptit.edu.vn.bookshop.domain.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.constant.OrderStatusEnum;
import ptit.edu.vn.bookshop.domain.dto.response.OrderResponseDTO;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageResponseDTO extends PageResponseAbstractDTO {
    List<OrderResponseDTO> orders;

}
