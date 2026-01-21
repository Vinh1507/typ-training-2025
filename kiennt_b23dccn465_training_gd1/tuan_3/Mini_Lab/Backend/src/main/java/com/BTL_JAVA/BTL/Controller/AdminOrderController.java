package com.BTL_JAVA.BTL.Controller;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Response.OrderResponse;
import com.BTL_JAVA.BTL.Service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/all-orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class AdminOrderController {
    OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<OrderResponse>> getAllOrderFromAllUser() {
        return ApiResponse.<List<OrderResponse>>ok(
                orderService.getAllOrderFromAllUser(), 
                "Lấy danh sách đơn hàng thành công!"
        );
    }
}
