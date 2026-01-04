package com.BTL_JAVA.BTL.DTO.Response;

import com.BTL_JAVA.BTL.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Integer id;
    Integer userId;
    String userFullName;
    String fullAddress;
    String phoneNumber;
    String note;
    LocalDateTime orderDate;
    OrderStatus status;
    Double totalAmount;
    List<OrderDetailResponse> orderDetails;
    @Data
    @Builder
    public static class OrderDetailResponse {
        String productName;
        Integer productId;
        Integer variationId;
        String color;
        String size;
        String image;
        Double price;
        int quantity;
    }
}

