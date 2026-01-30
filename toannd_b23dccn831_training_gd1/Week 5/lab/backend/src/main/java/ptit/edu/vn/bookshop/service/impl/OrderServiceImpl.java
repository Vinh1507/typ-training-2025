package ptit.edu.vn.bookshop.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.edu.vn.bookshop.domain.constant.BookStatusEnum;
import ptit.edu.vn.bookshop.domain.constant.OrderStatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.OrderCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.OrderUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.UpdateStatusRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.OrderResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.OrderPageResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.*;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.exception.UsernameNotFoundException;
import ptit.edu.vn.bookshop.repository.*;
import ptit.edu.vn.bookshop.repository.specification.OrderSpecificationBuilder;
import ptit.edu.vn.bookshop.service.OrderService;
import ptit.edu.vn.bookshop.service.UserService;
import ptit.edu.vn.bookshop.domain.dto.mapper.OrderMapper;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final BookRepository bookRepository;
    private final AddressRepository addressRepository;
    private final CouponRepository couponRepository;

    public OrderServiceImpl(UserService userService, CartRepository cartRepository, OrderRepository orderRepository,
                            CartItemRepository cartItemRepository, OrderMapper orderMapper, BookRepository bookRepository,
                            AddressRepository addressRepository, CouponRepository couponRepository) {
        this.userService = userService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderMapper = orderMapper;
        this.bookRepository = bookRepository;
        this.addressRepository = addressRepository;
        this.couponRepository = couponRepository;
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderCreateRequestDTO orderRequestDTO) {
        // lay thong tin cua nguoi dung len
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        User user = this.userService.getUserByUsername(email);
        // lay len thong tin cua gio hang
        Cart cart = this.cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user"));

        List<Long> cartItemIds = orderRequestDTO.getCartItems().stream()
                .map(OrderCreateRequestDTO.ItemRequestDTO::getId).toList();

        List<CartItem> items = cart.getCartItems().stream()
                .filter(it -> cartItemIds.contains(it.getId())).toList();

        if (items.size() != cartItemIds.size()) {
            throw new IllegalArgumentException("Some cart items are invalid or do not belong to the current cart");
        }
        // lấy thông tin địa chỉ
        Address address = this.addressRepository.findByUserAndIsDefaultTrue(user)
                .orElseThrow(() -> new IllegalArgumentException("Address not found for user"));

        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getPhone());
        order.setCity(address.getCity());
        order.setDistrict(address.getDistrict());
        order.setWard(address.getWard());
        order.setStreet(address.getStreet());

        order.setOrderDate(Instant.now());
        order.setPaymentMethod("COD");
        order.setNotes(orderRequestDTO.getNote());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            BigDecimal discountPercent = cartItem.getItemDiscount()
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal discountedPrice = cartItem.getUnitPrice()
                    .multiply(BigDecimal.ONE.subtract(discountPercent)).setScale(0, RoundingMode.HALF_UP);
            orderItem.setPrice(discountedPrice);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setBook(cartItem.getBook());
            orderItems.add(orderItem);

            // update book
            Book book = cartItem.getBook();
            int newQuantity = book.getQuantity() - cartItem.getQuantity();
            book.setQuantity(Math.max(newQuantity, 0));
            if (book.getQuantity() <= 0) {
                book.setStatus(BookStatusEnum.OUT_OF_STOCK);
            }
        }
        order.setOrderItems(orderItems);

        BigDecimal totalPrice = orderItems.stream()
                .map(it -> it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //coupon
        BigDecimal discountFee = BigDecimal.ZERO;
        if(orderRequestDTO.getCouponCode() != null) {
            Coupon coupon = this.couponRepository.findByCode(orderRequestDTO.getCouponCode())
                    .orElseThrow(() -> new IdInvalidException("Coupon not found"));
            if (coupon != null) {
                switch (coupon.getDiscountType()) {
                    case PERCENTAGE -> {
                        discountFee = totalPrice
                                .multiply(coupon.getDiscountValue()
                                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP));
                        // Giới hạn mức giảm nếu có maximumDiscountAmount
                        if (coupon.getMaximumDiscountAmount() != null &&
                                discountFee.compareTo(coupon.getMaximumDiscountAmount()) > 0) {
                            discountFee = coupon.getMaximumDiscountAmount();
                        }
                    }
                    case FIXED_AMOUNT -> {
                        discountFee = coupon.getDiscountValue();
                    }
                    default -> discountFee = BigDecimal.ZERO;
                }
            }
        }
        //hard code
        BigDecimal shippingFee = BigDecimal.valueOf(10000);
        if (totalPrice.compareTo(shippingFee) < 0) {
            totalPrice = BigDecimal.ZERO;
        }
        BigDecimal finalPrice = totalPrice.add(shippingFee).subtract(discountFee);
        if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
            finalPrice = BigDecimal.ZERO;
        }
        finalPrice = finalPrice.setScale(0, RoundingMode.HALF_UP);


        order.setTotalPrice(totalPrice);
        order.setShippingFee(shippingFee);
        order.setDiscountFee(discountFee);
        order.setFinalPrice(finalPrice);

        this.orderRepository.save(order);

        // xóa các sản phẩm mà khách hàng mua
        for (CartItem cartItem : items) {
            cart.getCartItems().remove(cartItem);
            this.cartItemRepository.delete(cartItem);
        }

        // Nếu giỏ hàng trống thì xóa cart
        if (cart.getCartItems().isEmpty()) {
            this.cartRepository.delete(cart);
        } else {
            // Nếu còn items, cập nhật lại cart
            this.cartRepository.save(cart);
        }
        return this.orderMapper.toOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO updateOrder(OrderUpdateRequestDTO orderRequestDTO, Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (orderRequestDTO.getReceiverName() != null && !orderRequestDTO.getReceiverName().isEmpty()) {
            order.setReceiverName(orderRequestDTO.getReceiverName());
        }
        if (orderRequestDTO.getReceiverPhone() != null && !orderRequestDTO.getReceiverPhone().isEmpty()) {
            order.setReceiverPhone(orderRequestDTO.getReceiverPhone());
        }
        if (orderRequestDTO.getCity() != null && !orderRequestDTO.getCity().isEmpty()) {
            order.setCity(orderRequestDTO.getCity());
        }
        if (orderRequestDTO.getDistrict() != null && !orderRequestDTO.getDistrict().isEmpty()) {
            order.setDistrict(orderRequestDTO.getDistrict());
        }
        if (orderRequestDTO.getWard() != null && !orderRequestDTO.getWard().isEmpty()) {
            order.setWard(orderRequestDTO.getWard());
        }
        if (orderRequestDTO.getStreet() != null && !orderRequestDTO.getStreet().isEmpty()) {
            order.setStreet(orderRequestDTO.getStreet());
        }
        if (orderRequestDTO.getNote() != null && !orderRequestDTO.getNote().isEmpty()) {
            order.setNotes(orderRequestDTO.getNote());
        }

        this.orderRepository.save(order);
        return this.orderMapper.toOrderResponseDTO(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getStatus() == OrderStatusEnum.PENDING) {
            order.setStatus(OrderStatusEnum.CANCELLED);
            this.orderRepository.save(order);
        }
    }

    @Override
    public OrderResponseDTO getOrder(Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Order not found"));
        return this.orderMapper.toOrderResponseDTO(order);
    }

    @Override
    public OrderPageResponseDTO getAllOrders(Pageable pageable, String[] orders) {
        Page<Order> orderPage;
        if (orders != null && orders.length > 0) {
            OrderSpecificationBuilder builder = new OrderSpecificationBuilder();
            for (String order : orders) {
                Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
                Matcher matcher = pattern.matcher(order);
                if (matcher.find()) {
                    builder.with(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5));
                }
            }
            orderPage = this.orderRepository.findAll(builder.build(), pageable);
        } else {
            orderPage = this.orderRepository.findAll(pageable);
        }
        OrderPageResponseDTO orderPageResponseDTO = new OrderPageResponseDTO();
        orderPageResponseDTO.setPage(orderPage.getNumber() + 1);
        orderPageResponseDTO.setTotal(orderPage.getTotalElements());
        orderPageResponseDTO.setPageSize(orderPage.getSize());
        orderPageResponseDTO.setPages(orderPage.getTotalPages());
        orderPageResponseDTO.setOrders(orderPage.stream().map(orderMapper::toOrderResponseDTO).collect(Collectors.toList()));
        return orderPageResponseDTO;
    }

    @Override
    public OrderResponseDTO updateOrderStatus(UpdateStatusRequestDTO updateStatusRequestDTO, Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(() -> new IdInvalidException("Order not found"));
        order.setStatus(updateStatusRequestDTO.getStatus());
        return this.orderMapper.toOrderResponseDTO(this.orderRepository.save(order));
    }
}
