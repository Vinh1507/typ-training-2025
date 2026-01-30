package ptit.edu.vn.bookshop.domain.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptit.edu.vn.bookshop.domain.dto.response.CouponResponseDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponPageResponseDTO extends PageResponseAbstractDTO{
    List<CouponResponseDTO> coupons;
}
