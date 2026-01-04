package com.BTL_JAVA.BTL.Service.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Product.CartItemRequest;
import com.BTL_JAVA.BTL.DTO.Request.Product.UpdateQuantityRequest;
import com.BTL_JAVA.BTL.DTO.Response.Product.CartItemResponse;
import com.BTL_JAVA.BTL.Entity.Product.Cart;
import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Repository.CartRepository;
import com.BTL_JAVA.BTL.Repository.ProductVariationRepository;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    CartRepository cartRepository;
    ProductVariationRepository productVariationRepository;
    UserRepository userRepository;

    public ApiResponse<List<CartItemResponse>> getCart() {
        try {
            User user = getCurrentUser();
            List<Cart> cartItems = cartRepository.findAll().stream()
                    .filter(cart -> cart.getUser().getId() == user.getId())
                    .collect(Collectors.toList());

            List<CartItemResponse> items = toCartItemResponseList(cartItems);

            return ApiResponse.<List<CartItemResponse>>builder()
                    .result(items)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }
    }

    public ApiResponse<List<CartItemResponse>> addToCart(CartItemRequest request) {
        try {
            User user = getCurrentUser();

            if (request.getQuantity() < 0) {
                throw new AppException(ErrorCode.INVALID_QUANTITY);
            }

            ProductVariation variation = productVariationRepository.findById(request.getProduct_variation_id())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIATION_NOT_FOUND));

            Cart existingCart = findCartItemByUserAndVariation(user, variation);
            int totalQuantity = request.getQuantity();

            if (existingCart != null) {
                totalQuantity += existingCart.getQuantity();
            }

            if (totalQuantity > 0 && totalQuantity > variation.getStockQuantity()) {
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK);
            }

            if (existingCart != null) {
                if (totalQuantity == 0) {
                    cartRepository.delete(existingCart);
                } else {
                    existingCart.setQuantity(totalQuantity);
                    cartRepository.save(existingCart);
                }
            } else {
                if (totalQuantity > 0) {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .productVariation(variation)
                            .quantity(totalQuantity)
                            .build();
                    cartRepository.save(newCart);
                }
            }
            return getCart();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CART_UPDATE_FAILED);
        }
    }


    public ApiResponse<List<CartItemResponse>> updateCartItem(Integer productVariationId, UpdateQuantityRequest request) {
        try {
            User user = getCurrentUser();
            int quantity = request.getQuantity();

            if (quantity < 0) {
                throw new AppException(ErrorCode.INVALID_QUANTITY);
            }

            ProductVariation variation = productVariationRepository.findById(productVariationId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIATION_NOT_FOUND));

            if (quantity > 0 && quantity > variation.getStockQuantity()) {
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK);
            }

            Cart existingCart = findCartItemByUserAndVariation(user, variation);

            if (existingCart != null) {
                if (quantity == 0) {
                    cartRepository.delete(existingCart);
                } else {
                    existingCart.setQuantity(quantity);
                    cartRepository.save(existingCart);
                }
            } else if (quantity > 0) {
                Cart newCart = Cart.builder()
                        .user(user)
                        .productVariation(variation)
                        .quantity(quantity)
                        .build();
                cartRepository.save(newCart);
            }

            return getCart();

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CART_UPDATE_FAILED);
        }
    }


    public ApiResponse<List<CartItemResponse>> removeFromCart(Integer productVariationId) {
        try {
            User user = getCurrentUser();

            ProductVariation variation = productVariationRepository.findById(productVariationId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIATION_NOT_FOUND));

            Cart existingCart = findCartItemByUserAndVariation(user, variation);

            if (existingCart != null) {
                cartRepository.delete(existingCart);
            }

            return getCart();

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CART_UPDATE_FAILED);
        }
    }

    private User getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByFullName(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return user;
    }

    private Cart findCartItemByUserAndVariation(User user, ProductVariation variation) {
        return cartRepository.findAll().stream()
                .filter(cart -> cart.getUser().getId() == user.getId() &&
                        cart.getProductVariation().getId() == variation.getId())
                .findFirst()
                .orElse(null);
    }

    private List<CartItemResponse> toCartItemResponseList(List<Cart> cartItems) {
        return cartItems.stream()
                .map(cart -> CartItemResponse.builder()
                        .product_id(cart.getProductVariation().getProduct().getProductId())
                        .product_variation_id(cart.getProductVariation().getId())
                        .quantity(cart.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}