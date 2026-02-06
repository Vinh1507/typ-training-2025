package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.Order.OrderRequest;
import com.BTL_JAVA.BTL.DTO.Request.Order.OrderUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.OrderResponse;
import com.BTL_JAVA.BTL.Entity.Address;
import com.BTL_JAVA.BTL.Entity.Orders.Order;
import com.BTL_JAVA.BTL.Entity.Orders.OrderDetail;
import com.BTL_JAVA.BTL.Entity.Product.ProductSale;
import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.AddressRepository;
import com.BTL_JAVA.BTL.Repository.OrderRepository;
import com.BTL_JAVA.BTL.Repository.ProductSaleRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import com.BTL_JAVA.BTL.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderService {
    final OrderRepository orderRepository;
    final UserRepository userRepository;
    final AddressRepository addressRepository;
    final com.BTL_JAVA.BTL.Repository.ProductVariationRepository productVariationRepository;
    final ProductSaleRepository productSaleRepository;

    // Các phương thức lấy người và ktra quyền
    User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByFullName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    void checkAdminPermission(User user) {
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getNameRoles().equals("ADMIN"));
        if (!isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * Tính giá cuối cùng cho sản phẩm, có áp dụng sale nếu có
     * @param productId ID của sản phẩm
     * @param originalPrice Giá gốc
     * @return Giá sau khi áp dụng sale (nếu có)
     */
    private Double calculateFinalPrice(Integer productId, Double originalPrice) {
        LocalDateTime now = LocalDateTime.now();
        
        // Tìm tất cả các ProductSale đang active của product này (đã sort theo saleValue DESC)
        List<ProductSale> productSales = productSaleRepository.findActiveProductSaleByProductId(productId, now);
        
        // Nếu có sale, lấy discount lớn nhất (phần tử đầu tiên do đã sort DESC)
        if (!productSales.isEmpty()) {
            BigDecimal maxDiscount = productSales.get(0).getSaleValue();
            
            if (maxDiscount != null && maxDiscount.compareTo(BigDecimal.ZERO) > 0) {
                // Áp dụng discount: giá cuối = giá gốc * (1 - discount)
                // Ví dụ: discount = 0.2 (20%) => giá cuối = giá gốc * 0.8
                BigDecimal discountMultiplier = BigDecimal.ONE.subtract(maxDiscount);
                double finalPrice = originalPrice * discountMultiplier.doubleValue();
                
                log.info("Product {} có sale {}% - Giá gốc: {}, Giá sale: {}", 
                        productId, maxDiscount.multiply(BigDecimal.valueOf(100)), originalPrice, finalPrice);
                
                return finalPrice;
            }
        }
        
        return originalPrice;
    }

    // tạo Order
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        // 1. Lấy thông tin user
        User user = getCurrentAuthenticatedUser();

        // 2. Lấy địa chỉ của user
        Address address = addressRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        // 3. Tạo đơn hàng
        Order order = Order.builder()
                .user(user)
                .fullAddress(address.getStreet() + ", " + address.getWard() + ", " + address.getCity())
                .phoneNumber(user.getPhoneNumber())
                .note(request.getNote())
                .status(OrderStatus.PENDING)
                .build();

        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalAmount = 0;

        // 4. Xử lý từng item trong đơn hàng
        for (OrderRequest.Item item : request.getItems()) {
            // Lấy ProductVariation từ database
            ProductVariation variation = productVariationRepository.findById(item.getVariationId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIATION_NOT_FOUND));

            // Kiểm tra tồn kho
            if (variation.getStockQuantity() < item.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }

            // Tính giá có áp dụng sale
            Double originalPrice = variation.getProduct().getPrice();
            Double finalPrice = calculateFinalPrice(variation.getProduct().getProductId(), originalPrice);

            // Tạo OrderDetail
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .productVariation(variation)
                    .quantity(item.getQuantity())
                    .price(finalPrice)
                    .build();
            orderDetails.add(orderDetail);

            // Cộng tổng tiền
            totalAmount += finalPrice * item.getQuantity();

            // Trừ số lượng tồn kho
            variation.setStockQuantity(variation.getStockQuantity() - item.getQuantity());
        }

        // 5. Set thông tin cho order và lưu
        order.setOrderDetails(orderDetails);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    // Lấy danh sách đơn hàng của user - Tối ưu với JOIN FETCH
    public List<OrderResponse> getUserOrderList() {
        User currentUser = getCurrentAuthenticatedUser();

        return orderRepository.findByUserWithDetails(currentUser).stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // Hủy đơn hàng - Tối ưu với JOIN FETCH
    @Transactional
    public OrderResponse cancelOrder(Integer orderId) {
        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        User user = getCurrentAuthenticatedUser();

        // Kiểm tra quyền: Admin có thể hủy tất cả, user thường chỉ hủy của mình
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getNameRoles().equals("ADMIN"));
        
        if (!isAdmin && order.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.APPROVED) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_ORDER);
        }

        for (OrderDetail detail : order.getOrderDetails()) {
            ProductVariation variation = detail.getProductVariation();
            variation.setStockQuantity(variation.getStockQuantity() + detail.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELED);
        Order cancelOrder = orderRepository.save(order);
        return mapToOrderResponse(cancelOrder);
    }

    // Chỉnh sửa Order - Có thể cập nhật địa chỉ, số điện thoại và ghi chú
    @Transactional
    public OrderResponse updateOrder(OrderUpdateRequest request, Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Kiểm tra xác thực
        User user = getCurrentAuthenticatedUser();

        // Kiểm tra quyền: Admin có thể cập nhật tất cả, user thường chỉ cập nhật của mình
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getNameRoles().equals("ADMIN"));
        
        if (!isAdmin && order.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Chỉ có thể cập nhật nếu đơn hàng đang ở trạng thái PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_ORDER);
        }

        // Cập nhật địa chỉ nếu có
        if (request.getAddressId() != null) {
            Address address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
            
            // Kiểm tra địa chỉ có thuộc về user không
            if (address.getUser().getId() != user.getId()) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            
            order.setFullAddress(address.getStreet() + ", " + address.getWard() + ", " + address.getCity());
        }

        // Cập nhật số điện thoại nếu có
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            order.setPhoneNumber(request.getPhoneNumber());
        }

        // Cập nhật ghi chú nếu có
        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }

        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponse(updatedOrder);
    }

    // Xem orderDetail - Tối ưu với JOIN FETCH
    @Transactional
    public OrderResponse getOrderById(Integer orderId) {
        User currentUser = getCurrentAuthenticatedUser();

        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getNameRoles().equals("ADMIN"));
        
        if (!isAdmin && order.getUser().getId() != currentUser.getId()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return mapToOrderResponse(order);
    }

    // Lấy tất cả order từ tất cả user - Tối ưu với JOIN FETCH
    @Transactional
    public List<OrderResponse> getAllOrderFromAllUser() {
        User currentUser = getCurrentAuthenticatedUser();

        checkAdminPermission(currentUser);

        return orderRepository.findAllWithDetails().stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // CHỉnh sửa status - Load details để return đầy đủ thông tin
    @Transactional
    public OrderResponse updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        User user = getCurrentAuthenticatedUser();

        checkAdminPermission(user);

        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setStatus(orderStatus);
        Order updatedOrder = orderRepository.save(order);

        return mapToOrderResponse(updatedOrder);
    }

    // Xóa một order
    @Transactional
   public void deleteOrder(Integer orderId) {
        User user = getCurrentAuthenticatedUser();

        checkAdminPermission(user);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        orderRepository.delete(order);
    }

    // Phân trang cho orders của user - Tối ưu cho dữ liệu lớn
    public Page<OrderResponse> getAllOrderByUserIdPaginated(int page, int size) {
        User currentUser = getCurrentAuthenticatedUser();
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        Page<Order> orderPage = orderRepository.findByUser(currentUser, pageable);
        
        return orderPage.map(this::mapToOrderResponse);
    }
    
    // Lấy orders theo status - Tối ưu để filter
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        User currentUser = getCurrentAuthenticatedUser();
        checkAdminPermission(currentUser);
        
        return orderRepository.findByStatusWithDetails(status).stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse mapToOrderResponse(Order order) {
        List<OrderResponse.OrderDetailResponse> detailResponses = order.getOrderDetails().stream()
                .map(detail -> OrderResponse.OrderDetailResponse.builder()
                        .productName(detail.getProductVariation().getProduct().getTitle())
                        .productId(detail.getProductVariation().getProduct().getProductId())
                        .variationId(detail.getProductVariation().getId())
                        .color(detail.getProductVariation().getColor())
                        .size(detail.getProductVariation().getSize())
                        .image(detail.getProductVariation().getImage())
                        .price(detail.getPrice())
                        .quantity(detail.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userFullName(order.getUser().getFullName())
                .fullAddress(order.getFullAddress())
                .phoneNumber(order.getPhoneNumber())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDetails(detailResponses)
                .build();
    }
}
