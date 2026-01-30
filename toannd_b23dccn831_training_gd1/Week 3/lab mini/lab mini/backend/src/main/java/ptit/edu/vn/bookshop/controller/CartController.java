package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.AddCartItemRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CartResponseDTO;
import ptit.edu.vn.bookshop.service.CartService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/items")
    @ApiMessage("Item added to cart successfully")
    public ResponseEntity<CartResponseDTO> addToCart(@Valid @RequestBody AddCartItemRequestDTO addCartItemRequestDTO) {
        CartResponseDTO cartResponseDTO = this.cartService.addItemToCart(addCartItemRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponseDTO);
    }

    @PatchMapping("/carts/items/{id}")
    @ApiMessage("Cart item updated successfully")
    public ResponseEntity<CartResponseDTO> updateCart(@Valid @Min(1) @RequestParam Integer quantity,
                                                      @PathVariable Long id) {
        CartResponseDTO cartResponseDTO = this.cartService.updateCart(quantity, id);
        return ResponseEntity.ok().body(cartResponseDTO);
    }

    @DeleteMapping("/carts/items/{id}")
    @ApiMessage("Cart item removed successfully")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        this.cartService.removeCartItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/carts")
    @ApiMessage("Cart items retrieved successfully")
    public ResponseEntity<CartResponseDTO> getAllCartItems() {
        CartResponseDTO cartResponseDTO = this.cartService.getCartItems();
        return ResponseEntity.ok().body(cartResponseDTO);
    }
}