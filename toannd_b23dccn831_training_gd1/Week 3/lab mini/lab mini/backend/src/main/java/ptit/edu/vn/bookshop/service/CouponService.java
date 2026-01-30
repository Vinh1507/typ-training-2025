package ptit.edu.vn.bookshop.service;


import org.springframework.data.domain.Pageable;
import ptit.edu.vn.bookshop.domain.dto.request.CouponCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.CouponUpdateRequestDTO;;
import ptit.edu.vn.bookshop.domain.dto.response.CouponResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.CouponPageResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.Coupon;


public interface CouponService {
    CouponResponseDTO createCoupon(CouponCreateRequestDTO couponRequestDTO);
    CouponResponseDTO updateCoupon(CouponUpdateRequestDTO couponRequestDTO, Long id);
    void deleteCoupon(Long id);
    CouponResponseDTO getCoupon(Long id, boolean admin);
    CouponPageResponseDTO getAllCoupons(Pageable pageable, String[] coupon, boolean isAdmin);
    Coupon getCouponByCode(String code);
}
