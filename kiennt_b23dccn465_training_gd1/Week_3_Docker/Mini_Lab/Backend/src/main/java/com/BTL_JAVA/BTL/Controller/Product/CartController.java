package com.BTL_JAVA.BTL.Controller.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Product.CartItemRequest;
import com.BTL_JAVA.BTL.DTO.Request.Product.UpdateQuantityRequest;
import com.BTL_JAVA.BTL.DTO.Response.Product.CartItemResponse;
import com.BTL_JAVA.BTL.Service.Product.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<CartItemResponse>> getCart() {
        return cartService.getCart();
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<CartItemResponse>> addToCart(@RequestBody CartItemRequest request) {
        return cartService.addToCart(request);
    }

    @PutMapping("/{productVariationId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<CartItemResponse>> updateCartItem(
            @PathVariable Integer productVariationId,
            @RequestBody UpdateQuantityRequest request) {
        return cartService.updateCartItem(productVariationId, request);
    }

    @DeleteMapping("/{productVariationId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<CartItemResponse>> removeFromCart(@PathVariable Integer productVariationId) {
        return cartService.removeFromCart(productVariationId);
    }
}