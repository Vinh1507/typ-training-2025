package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.springframework.stereotype.Component;
import ptit.edu.vn.bookshop.domain.dto.response.CartResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Cart;
import ptit.edu.vn.bookshop.domain.entity.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartResponseDTO.CartItemResponseDTO mapCartItemToResponseDTO(CartItem item) {
        CartResponseDTO.CartItemResponseDTO dto = new CartResponseDTO.CartItemResponseDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getBook().getId());
        dto.setProductName(item.getBook().getName());
        dto.setProductStatus(item.getBook().getStatus());
        dto.setImageUrl(item.getBook().getImage());
        dto.setUnitPrice(item.getUnitPrice().setScale(0, RoundingMode.HALF_UP));
        dto.setQuantity(item.getQuantity());
        BigDecimal discount = item.getItemDiscount().setScale(0, RoundingMode.HALF_UP);
        dto.setDiscount(discount);

        BigDecimal discountedPrice = item.getUnitPrice();

        if (discount != null && discount.compareTo(BigDecimal.ZERO) >= 0) {
            BigDecimal discountRate = discount.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            discountedPrice = item.getUnitPrice().multiply(discountRate)
                    .setScale(0, RoundingMode.HALF_UP);
        }

        dto.setDiscountedPrice(discountedPrice);

        BigDecimal totalPrice = item.getUnitPrice().subtract(discountedPrice);

        dto.setFinalPrice(totalPrice);

        return dto;
    }

    public CartResponseDTO mapCartToResponseDTO(Cart cart) {
        List<CartResponseDTO.CartItemResponseDTO> itemDTOs = cart.getCartItems().stream()
                .sorted(Comparator.comparing(CartItem::getCreatedAt))
                .map(this::mapCartItemToResponseDTO)
                .collect(Collectors.toList());

        CartResponseDTO.CartSummaryDTO summary = new CartResponseDTO.CartSummaryDTO();

        int totalQuantity = itemDTOs.stream()
                .mapToInt(CartResponseDTO.CartItemResponseDTO::getQuantity)
                .sum();

        BigDecimal subtotal = itemDTOs.stream()
                .map(it -> it.getFinalPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(0, RoundingMode.HALF_UP);

        summary.setTotalQuantity(totalQuantity);
        summary.setSubtotal(subtotal);

        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(Instant.now());
        dto.setCartItems(itemDTOs);
        dto.setSummary(summary);

        return dto;
    }

}
