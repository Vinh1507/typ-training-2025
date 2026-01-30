package ptit.edu.vn.bookshop.service;

import ptit.edu.vn.bookshop.domain.dto.request.CheckoutRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CheckoutResponseDTO;

public interface CheckoutService {
    CheckoutResponseDTO getCheckout(CheckoutRequestDTO checkoutRequestDTO);
}
