package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.CouponCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.CouponUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.CouponResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.CouponPageResponseDTO;
import ptit.edu.vn.bookshop.service.CouponService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CouponController {

    private CouponService couponService;
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/coupons")
    @ApiMessage("")
    public ResponseEntity<CouponResponseDTO> createCoupon(
            @Valid @RequestBody CouponCreateRequestDTO couponRequestDTO) {
        CouponResponseDTO couponResponse = this.couponService.createCoupon(couponRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponResponse);
    }

    @PutMapping("/coupons/{id}")
    @ApiMessage("")
    public ResponseEntity<CouponResponseDTO> updateCoupon(
            @Valid @RequestBody CouponUpdateRequestDTO couponRequestDTO,
            @PathVariable Long id) {
        CouponResponseDTO couponResponse = this.couponService.updateCoupon(couponRequestDTO, id);
        return ResponseEntity.ok().body(couponResponse);
    }

    @DeleteMapping("/coupons/{id}")
    @ApiMessage("")
    public ResponseEntity<Void>  deleteCoupon(@PathVariable Long id) {
        this.couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/coupons/{id}")
    @ApiMessage("")
    public ResponseEntity<CouponResponseDTO> getCoupon(@PathVariable Long id) {
        CouponResponseDTO couponResponseDTO = this.couponService.getCoupon(id, false);
        return ResponseEntity.ok().body(couponResponseDTO);
    }

    @GetMapping("/coupons")
    @ApiMessage("")
    public ResponseEntity<CouponPageResponseDTO> getAllCoupons(
            Pageable pageable,
            @RequestParam(required = false) String[] coupon)
    {
        CouponPageResponseDTO couponPageResponseDTO = this.couponService.getAllCoupons(pageable, coupon, false);
        return ResponseEntity.ok().body(couponPageResponseDTO);
    }

    @GetMapping("/admin/coupons/{id}")
    @ApiMessage("")
    public ResponseEntity<CouponResponseDTO> getCouponByAdmin(@PathVariable Long id) {
        CouponResponseDTO couponResponseDTO = this.couponService.getCoupon(id, true);
        return ResponseEntity.ok().body(couponResponseDTO);
    }

    @GetMapping("/admin/coupons")
    @ApiMessage("")
    public ResponseEntity<CouponPageResponseDTO> getAllCouponsByAdmin(
            Pageable pageable,
            @RequestParam(required = false) String[] coupon)
    {
        CouponPageResponseDTO couponPageResponseDTO = this.couponService.getAllCoupons(pageable, coupon, true);
        return ResponseEntity.ok().body(couponPageResponseDTO);
    }

}
