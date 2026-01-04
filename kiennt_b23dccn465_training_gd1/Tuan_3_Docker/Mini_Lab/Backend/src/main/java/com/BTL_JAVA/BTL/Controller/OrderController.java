package com.BTL_JAVA.BTL.Controller;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Order.OrderRequest;
import com.BTL_JAVA.BTL.DTO.Request.Order.OrderUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.OrderResponse;
import com.BTL_JAVA.BTL.Service.OrderService;
import com.BTL_JAVA.BTL.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class OrderController {
    OrderService orderService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    ApiResponse<List<OrderResponse>> getUserOrderList() {
        return ApiResponse.<List<OrderResponse>>ok(
                orderService.getUserOrderList(), 
                "Lấy danh sách đơn hàng thành công!"
        );
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ApiResponse.<OrderResponse>ok(
                orderService.createOrder(request), 
                "Tạo đơn hàng thành công!"
        );
    }

    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<OrderResponse> cancelOrder(@PathVariable("orderId") Integer orderId) {
        return ApiResponse.<OrderResponse>ok(
                orderService.cancelOrder(orderId), 
                "Hủy đơn hàng thành công!"
        );
    }

    @PatchMapping("/{orderId}/update")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<OrderResponse> updateOrder(@RequestBody OrderUpdateRequest request, @PathVariable("orderId") Integer orderId) {
        return ApiResponse.<OrderResponse>ok(
                orderService.updateOrder(request, orderId), 
                "Cập nhật đơn hàng thành công!"
        );
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<OrderResponse> getOrderById(@PathVariable("orderId") Integer orderId) {
        return ApiResponse.<OrderResponse>ok(
                orderService.getOrderById(orderId), 
                "Lấy chi tiết đơn hàng thành công!"
        );
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable("orderId") Integer orderId, 
            @RequestParam("status") OrderStatus status) {
        return ApiResponse.<OrderResponse>ok(
                orderService.updateOrderStatus(orderId, status), 
                "Cập nhật trạng thái đơn hàng thành công!"
        );
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<Void> deleteOrder(@PathVariable("orderId") Integer orderId) {
        orderService.deleteOrder(orderId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xóa đơn hàng thành công!")
                .build();
    }
    
    // API phân trang - Tối ưu cho dữ liệu lớn
    @GetMapping("/paginated")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<Page<OrderResponse>> getAllOrdersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<OrderResponse>>ok(
                orderService.getAllOrderByUserIdPaginated(page, size), 
                "Lấy danh sách đơn hàng phân trang thành công!"
        );
    }
    
    // API lấy orders theo status - Tối ưu để filter
    @GetMapping("/status/{status}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ApiResponse.<List<OrderResponse>>ok(
                orderService.getOrdersByStatus(status), 
                "Lấy đơn hàng theo trạng thái thành công!"
        );
    }

}
