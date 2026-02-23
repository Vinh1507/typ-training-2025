package com.BTL_JAVA.BTL.Controller;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Review.ReviewRequest;
import com.BTL_JAVA.BTL.DTO.Response.Review.ReviewResponse;
import com.BTL_JAVA.BTL.DTO.Response.Review.UserReviewsResponse;
import com.BTL_JAVA.BTL.Service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class ReviewController {
    ReviewService reviewService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ApiResponse<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .code(1000)
                .message("Tạo review thành công!")
                .result(reviewService.createReview(request))
                .build();
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<ReviewResponse> updateReview(@PathVariable("reviewId") Integer reviewId, @RequestBody ReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .code(1000)
                .message("Cập nhật review thành công!")
                .result(reviewService.updateReview(reviewId, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ReviewResponse>> getAllReviews() {
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(1000)
                .message("Lấy danh sách reviews thành công!")
                .result(reviewService.getAllReview())
                .build();
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<Void> deleteReview(@PathVariable("reviewId") Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xóa review thành công!")
                .build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<UserReviewsResponse> getAllReviewByUserId(@PathVariable("userId") Integer userId) {
        return ApiResponse.<UserReviewsResponse>builder()
                .code(1000)
                .message("Lấy reviews của user thành công!")
                .result(reviewService.getReviewByUserId(userId))
                .build();
    }
    
    // API phân trang - Tối ưu cho dữ liệu lớn
    @GetMapping("/paginated")
    ApiResponse<Page<ReviewResponse>> getAllReviewsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<ReviewResponse>>builder()
                .code(1000)
                .message("Lấy danh sách reviews phân trang thành công!")
                .result(reviewService.getAllReviewsPaginated(page, size))
                .build();
    }
    
    // API phân trang cho reviews của user cụ thể
    @GetMapping("/user/{userId}/paginated")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<Page<ReviewResponse>> getReviewsByUserIdPaginated(
            @PathVariable("userId") int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<ReviewResponse>>builder()
                .code(1000)
                .message("Lấy reviews của user phân trang thành công!")
                .result(reviewService.getReviewsByUserIdPaginated(userId, page, size))
                .build();
    }
    
    // Lấy reviews theo rating cụ thể (1-5 sao)
    @GetMapping("/rating/{rating}")
    ApiResponse<List<ReviewResponse>> getReviewsByRating(@PathVariable("rating") Integer rating) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(1000)
                .message("Lấy reviews theo rating " + rating + " sao thành công!")
                .result(reviewService.getReviewsByRating(rating))
                .build();
    }
    
    // Lấy reviews có rating >= giá trị cho trước
    @GetMapping("/rating/min/{minRating}")
    ApiResponse<List<ReviewResponse>> getReviewsByMinRating(@PathVariable("minRating") Integer minRating) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(1000)
                .message("Lấy reviews >= " + minRating + " sao thành công!")
                .result(reviewService.getReviewsByMinRating(minRating))
                .build();
    }
    
    // Lấy tất cả reviews có rating
    @GetMapping("/with-rating")
    ApiResponse<List<ReviewResponse>> getAllReviewsWithRating() {
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(1000)
                .message("Lấy tất cả reviews có rating thành công!")
                .result(reviewService.getAllReviewsWithRating())
                .build();
    }
    
    // Lấy reviews không có rating (reviews cũ)
    @GetMapping("/without-rating")
    ApiResponse<List<ReviewResponse>> getAllReviewsWithoutRating() {
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(1000)
                .message("Lấy reviews không có rating thành công!")
                .result(reviewService.getAllReviewsWithoutRating())
                .build();
    }
}
