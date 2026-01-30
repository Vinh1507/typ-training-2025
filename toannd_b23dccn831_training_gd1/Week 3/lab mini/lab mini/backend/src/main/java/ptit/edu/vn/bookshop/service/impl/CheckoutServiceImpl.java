package ptit.edu.vn.bookshop.service.impl;

import org.springframework.stereotype.Service;
import ptit.edu.vn.bookshop.domain.dto.request.CheckoutRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.OrderCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CheckoutResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.*;
import ptit.edu.vn.bookshop.exception.UsernameNotFoundException;
import ptit.edu.vn.bookshop.service.*;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final UserService userService;
    private final CartService cartService;
    private final AddressService addressService;
    private final CouponService couponService;

    public CheckoutServiceImpl(UserService userService, CartService cartService, AddressService addressService,
                               CouponService couponService) {
        this.userService = userService;
        this.cartService = cartService;
        this.addressService = addressService;
        this.couponService = couponService;
    }

    @Override
    public CheckoutResponseDTO getCheckout(CheckoutRequestDTO checkoutRequest) {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("User is not found."));
        User user = this.userService.getUserByUsername(email);
//         lấy thông tin về giỏ hàng
        Cart cart = this.cartService.getCartByUser(user.getId());

        List<Long> cartItemIds = checkoutRequest.getCartItems().stream()
                .map(CheckoutRequestDTO.CheckoutCartItemsRequestDTO::getId).toList();

        List<CartItem> cartItems = cart.getCartItems().stream()
                        .filter(it -> cartItemIds.contains(it.getId())).toList();

        List<CheckoutResponseDTO.CheckoutItemDTO> itemDTOS = new ArrayList<>();

        for (CartItem it : cartItems) {
            CheckoutResponseDTO.CheckoutItemDTO itemDTO = new CheckoutResponseDTO.CheckoutItemDTO();
            itemDTO.setProductId(it.getBook().getId());
            itemDTO.setProductName(it.getBook().getName());
            itemDTO.setImageUrl(it.getBook().getImage());
            itemDTO.setQuantity(it.getQuantity());
            itemDTO.setUnitPrice(it.getUnitPrice());
            BigDecimal discount = it.getItemDiscount().divide(BigDecimal.valueOf(100));
            BigDecimal finalPrice = it.getUnitPrice().multiply(BigDecimal.valueOf(1).subtract(discount))
                    .setScale(0, RoundingMode.HALF_UP);
            itemDTO.setFinalPrice(finalPrice);
            itemDTOS.add(itemDTO);
        }

        int quantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalPrice = itemDTOS.stream()
                .map(it -> it.getFinalPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // lấy lên thông tin về địa chỉ
        Address address = this.addressService.getAddressByIsDefault();
//      // lấy thông tin mã giảm giá
        Coupon coupon;
        BigDecimal discountFee = BigDecimal.ZERO;
        CheckoutResponseDTO.CouponInfo couponInfo = null;
        if(checkoutRequest.getCouponCode() != null) {
            coupon = this.couponService.getCouponByCode(checkoutRequest.getCouponCode());
            couponInfo = new CheckoutResponseDTO.CouponInfo();
            couponInfo.setCode(coupon.getCode());
            couponInfo.setDiscountValue(coupon.getDiscountValue());
            couponInfo.setDiscountType(coupon.getDiscountType());
            couponInfo.setExpiredAt(coupon.getExpiresAt());
            switch (coupon.getDiscountType()) {
                case PERCENTAGE -> {
                    discountFee = totalPrice.multiply(coupon.getDiscountValue()
                                            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
                            .setScale(0, RoundingMode.HALF_UP);
                    // Giới hạn mức giảm nếu có maximumDiscountAmount
                    if (coupon.getMaximumDiscountAmount() != null &&
                            discountFee.compareTo(coupon.getMaximumDiscountAmount()) > 0) {
                        discountFee = coupon.getMaximumDiscountAmount();
                    }
                }
                case FIXED_AMOUNT -> {
                    discountFee = coupon.getDiscountValue();
                    if (discountFee.compareTo(totalPrice) > 0) {
                        discountFee = totalPrice;
                    }
                }
                default -> discountFee = BigDecimal.ZERO;
            }
        }

        CheckoutResponseDTO.ShippingAddress shippingAddress = new CheckoutResponseDTO.ShippingAddress();
        shippingAddress.setName(address.getReceiverName());
        shippingAddress.setPhone(address.getPhone());
        String fullAddress  = Stream.of(
                        address.getStreet(),
                        address.getWard(),
                        address.getDistrict(),
                        address.getCity()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
        shippingAddress.setAddress(fullAddress);


        CheckoutResponseDTO.SummaryCheckout summaryCheckout = new CheckoutResponseDTO.SummaryCheckout();
        // hard code
        BigDecimal shippingFee = BigDecimal.valueOf(10_000);

        BigDecimal finalPrice = totalPrice.add(shippingFee).subtract(discountFee);

        summaryCheckout.setSubtotal(totalPrice);
        summaryCheckout.setTotalQuantity(quantity);
        summaryCheckout.setCartDiscount(discountFee);
        summaryCheckout.setShippingFee(shippingFee);
        summaryCheckout.setGrandTotal(finalPrice);

        CheckoutResponseDTO checkOutResponseDTO = new CheckoutResponseDTO();
        checkOutResponseDTO.setShippingAddress(shippingAddress);
        checkOutResponseDTO.setItems(itemDTOS);
        checkOutResponseDTO.setCouponInfo(couponInfo);
        checkOutResponseDTO.setPaymentMethods("COD");
        checkOutResponseDTO.setSummary(summaryCheckout);
        return checkOutResponseDTO;
    }
}
