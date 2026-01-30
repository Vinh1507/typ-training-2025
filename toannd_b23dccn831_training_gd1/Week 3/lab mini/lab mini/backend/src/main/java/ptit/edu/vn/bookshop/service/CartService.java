package ptit.edu.vn.bookshop.service;

import ptit.edu.vn.bookshop.domain.dto.request.AddCartItemRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CartResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Cart;

public interface CartService {
    CartResponseDTO addItemToCart(AddCartItemRequestDTO addCartItemRequestDTO);
    CartResponseDTO updateCart(Integer quantity, Long id);
    void removeCartItem(Long id);
    CartResponseDTO getCartItems();
    Cart getCartByUser(Long id);
}
