package ptit.edu.vn.bookshop.domain.dto.mapper;

import org.springframework.stereotype.Component;
import ptit.edu.vn.bookshop.domain.dto.response.OrderResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Order;
import ptit.edu.vn.bookshop.domain.entity.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OrderMapper {

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        // Shipping Info
        OrderResponseDTO.ShippingInfo shippingInfo = new OrderResponseDTO.ShippingInfo();
        shippingInfo.setReceiverName(order.getReceiverName());
        shippingInfo.setReceiverPhone(order.getReceiverPhone());
        String fullAddress = Stream.of(
                        order.getStreet(),
                        order.getWard(),
                        order.getDistrict(),
                        order.getCity()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
        shippingInfo.setReceiverAddress(fullAddress);

        // Payment


        // Summary
        OrderResponseDTO.Summary summary = new OrderResponseDTO.Summary();
        summary.setSubtotal(order.getTotalPrice());
        summary.setShippingFee(order.getShippingFee());
        summary.setDiscountFee(order.getDiscountFee());
        summary.setFinalPrice(order.getFinalPrice());

        // Items
        List<OrderResponseDTO.OrderItemResponse> orderItemResponseList = new ArrayList<>();
        for(OrderItem item : order.getOrderItems()) {
            OrderResponseDTO.OrderItemResponse orderItemResponse = toOrderItemResponse(item);
            orderItemResponseList.add(orderItemResponse);
        }

        // Final response
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(order.getId());
        responseDTO.setStatus(order.getStatus());
        responseDTO.setCreatedAt(order.getCreatedAt() != null ? order.getCreatedAt() : Instant.now());
        responseDTO.setShippingInfo(shippingInfo);
        responseDTO.setSummary(summary);
        responseDTO.setItems(orderItemResponseList);

        //payment
        responseDTO.setPaymentMethod(order.getPaymentMethod());

        return responseDTO;
    }

    public OrderResponseDTO.OrderItemResponse toOrderItemResponse(OrderItem orderItem) {

        OrderResponseDTO.OrderItemResponse itemResponse = new OrderResponseDTO.OrderItemResponse();

        itemResponse.setId(orderItem.getId());
        itemResponse.setProductId(orderItem.getBook().getId());
        itemResponse.setProductName(orderItem.getBook().getName());
        itemResponse.setImageUrl(orderItem.getBook().getImage());
        itemResponse.setQuantity(orderItem.getQuantity());
        itemResponse.setUnitPrice(orderItem.getBook().getPrice());
        itemResponse.setDiscount(orderItem.getBook().getDiscount());

        BigDecimal discountPercent = orderItem.getBook().getDiscount()
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal discountedPrice = orderItem.getBook().getPrice()
                .multiply(BigDecimal.ONE.subtract(discountPercent)).setScale(0, RoundingMode.HALF_UP);

        itemResponse.setDiscountedPrice(discountedPrice);

        BigDecimal totalPrice = discountedPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                .setScale(0, RoundingMode.HALF_UP);
        itemResponse.setTotalPrice(totalPrice);
        return itemResponse;
    }
}
