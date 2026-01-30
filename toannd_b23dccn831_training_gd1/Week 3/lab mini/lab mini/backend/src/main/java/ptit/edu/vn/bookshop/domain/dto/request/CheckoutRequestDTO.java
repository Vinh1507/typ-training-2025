package ptit.edu.vn.bookshop.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
public class CheckoutRequestDTO {
    private List<CheckoutCartItemsRequestDTO> cartItems;
    private String couponCode;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutCartItemsRequestDTO {
        private Long id;
    }
}
