package com.BTL_JAVA.BTL.Controller;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Review.FeedbackRequest;
import com.BTL_JAVA.BTL.DTO.Response.Review.FeedbackResponse;
import com.BTL_JAVA.BTL.DTO.Response.Review.ProductFeedbackSummary;
import com.BTL_JAVA.BTL.Service.FeedbackService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FeedbackController {
    FeedbackService feedbackService;

    // GET /feedback/{id_prod} - Lấy tất cả feedback của product (Public - không cần auth)
    @GetMapping("/{productId}")
    public ApiResponse<ProductFeedbackSummary> getAllFeedbackByProductId(
            @PathVariable("productId") Integer productId) {
        return ApiResponse.<ProductFeedbackSummary>ok(
                feedbackService.getAllFeedbackByProductId(productId),
                "Lấy feedback của sản phẩm thành công!"
        );
    }

    // POST /feedback/{id_prod} - Tạo feedback cho product (Authenticated)
    @PostMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<FeedbackResponse> createFeedback(
            @PathVariable("productId") Integer productId,
            @Valid @RequestBody FeedbackRequest request) {
        return ApiResponse.<FeedbackResponse>ok(
                feedbackService.createFeedback(productId, request),
                "Tạo feedback thành công!"
        );
    }

    // DELETE /feedback/{id_prod}/{id_fb} - Xóa feedback (Admin hoặc Owner)
    @DeleteMapping("/{productId}/{feedbackId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> deleteFeedback(
            @PathVariable("productId") Integer productId,
            @PathVariable("feedbackId") Integer feedbackId) {
        feedbackService.deleteFeedback(productId, feedbackId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Xóa feedback thành công!")
                .build();
    }

    // PUT /feedback/{id_prod}/{id_fb} - Cập nhật feedback (Owner)
    @PutMapping("/{productId}/{feedbackId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<FeedbackResponse> updateFeedback(
            @PathVariable("productId") Integer productId,
            @PathVariable("feedbackId") Integer feedbackId,
            @Valid @RequestBody FeedbackRequest request) {
        return ApiResponse.<FeedbackResponse>ok(
                feedbackService.updateFeedback(productId, feedbackId, request),
                "Cập nhật feedback thành công!"
        );
    }

    // GET /feedback/{id_prod}/paginated - Lấy feedback với phân trang (Public)
    @GetMapping("/{productId}/paginated")
    public ApiResponse<Page<FeedbackResponse>> getFeedbackPaginated(
            @PathVariable("productId") Integer productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.<Page<FeedbackResponse>>ok(
                feedbackService.getFeedbackByProductIdPaginated(productId, page, size),
                "Lấy feedback phân trang thành công!"
        );
    }

    // GET /feedback/my - Lấy tất cả feedback của user hiện tại (Authenticated)
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<FeedbackResponse>> getMyFeedbacks() {
        return ApiResponse.<List<FeedbackResponse>>ok(
                feedbackService.getMyFeedbacks(),
                "Lấy feedback của bạn thành công!"
        );
    }

    // GET /feedback/{id_prod}/rating/{rating} - Lấy feedback theo rating (Public)
    @GetMapping("/{productId}/rating/{rating}")
    public ApiResponse<List<FeedbackResponse>> getFeedbackByRating(
            @PathVariable("productId") Integer productId,
            @PathVariable("rating") Integer rating) {
        return ApiResponse.<List<FeedbackResponse>>ok(
                feedbackService.getFeedbackByProductIdAndRating(productId, rating),
                "Lấy feedback theo rating " + rating + " sao thành công!"
        );
    }
}

