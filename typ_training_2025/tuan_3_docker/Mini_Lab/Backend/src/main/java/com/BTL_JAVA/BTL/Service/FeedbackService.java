package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.Review.FeedbackRequest;
import com.BTL_JAVA.BTL.DTO.Response.Review.FeedbackResponse;
import com.BTL_JAVA.BTL.DTO.Response.Review.ProductFeedbackSummary;
import com.BTL_JAVA.BTL.Entity.Feedback;
import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.FeedbackRepository;
import com.BTL_JAVA.BTL.Repository.ProductRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackService {
    FeedbackRepository feedbackRepository;
    UserRepository userRepository;
    ProductRepository productRepository;

    // Lấy user hiện tại
    private User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByFullName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    // Kiểm tra quyền ADMIN
    private boolean isAdmin(User user) {
        return user.getRoles() != null && user.getRoles().stream()
                .anyMatch(role -> role.getNameRoles().equals("ADMIN"));
    }

    // Tạo feedback cho product (POST /feedback/{id_prod})
    @Transactional
    public FeedbackResponse createFeedback(Integer productId, FeedbackRequest request) {
        User user = getCurrentAuthenticatedUser();
        
        log.info("User {} creating feedback for product {}", user.getId(), productId);

        // Kiểm tra product có tồn tại
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Kiểm tra user đã feedback cho product này chưa
        if (feedbackRepository.hasUserFeedbackedProduct(user.getId(), productId)) {
            throw new AppException(ErrorCode.ALREADY_FEEDBACKED);
        }

        // Kiểm tra user đã mua product chưa (optional - có thể bật/tắt)
        boolean hasPurchased = feedbackRepository.hasUserPurchasedProduct(user.getId(), productId);
        if (!hasPurchased) {
            log.warn("User {} hasn't purchased product {} but creating feedback", user.getId(), productId);
            // Có thể throw exception hoặc cho phép feedback
            // throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Tạo feedback
        Feedback feedback = Feedback.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .note(request.getNote())
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("Feedback {} created successfully", savedFeedback.getId());

        return mapToResponse(savedFeedback);
    }

    // Lấy tất cả feedback của product (GET /feedback/{id_prod})
    public ProductFeedbackSummary getAllFeedbackByProductId(Integer productId) {
        // Kiểm tra product có tồn tại
        productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<Feedback> feedbacks = feedbackRepository.findByProductIdWithDetails(productId);
        
        // Lấy product name từ feedback đầu tiên hoặc từ repository
        String productName;
        if (!feedbacks.isEmpty()) {
            productName = feedbacks.get(0).getProduct().getTitle();
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            productName = product.getTitle();
        }

        // Tính toán thống kê
        Double averageRating = feedbackRepository.getAverageRatingByProductId(productId);
        Long totalFeedbacks = (long) feedbacks.size();

        // Phân bố rating
        Map<Integer, Long> ratingDistribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            Long count = feedbackRepository.countByProductIdAndRating(productId, i);
            ratingDistribution.put(i, count);
        }

        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ProductFeedbackSummary.builder()
                .productId(productId)
                .productName(productName)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .totalFeedbacks(totalFeedbacks)
                .ratingDistribution(ratingDistribution)
                .feedbacks(feedbackResponses)
                .build();
    }

    // Lấy feedback với phân trang
    public Page<FeedbackResponse> getFeedbackByProductIdPaginated(Integer productId, int page, int size) {
        // Kiểm tra product có tồn tại
        productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Feedback> feedbackPage = feedbackRepository.findByProductIdPaginated(productId, pageable);

        return feedbackPage.map(this::mapToResponse);
    }

    // Xóa feedback (DELETE /feedback/{id_prod}/{id_fb})
    @Transactional
    public void deleteFeedback(Integer productId, Integer feedbackId) {
        User currentUser = getCurrentAuthenticatedUser();

        Feedback feedback = feedbackRepository.findByIdWithDetails(feedbackId)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));

        // Kiểm tra feedback có thuộc product không
        if (feedback.getProduct().getProductId() != productId) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        // Chỉ admin hoặc chính user tạo feedback mới có quyền xóa
        boolean isAdminUser = isAdmin(currentUser);
        boolean isOwner = feedback.getUser().getId() == currentUser.getId();

        if (!isAdminUser && !isOwner) {
            log.error("User {} unauthorized to delete feedback {}", currentUser.getId(), feedbackId);
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        feedbackRepository.delete(feedback);
        log.info("Feedback {} deleted by user {}", feedbackId, currentUser.getId());
    }

    // Cập nhật feedback
    @Transactional
    public FeedbackResponse updateFeedback(Integer productId, Integer feedbackId, FeedbackRequest request) {
        User currentUser = getCurrentAuthenticatedUser();

        Feedback feedback = feedbackRepository.findByIdWithDetails(feedbackId)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));

        // Kiểm tra feedback có thuộc product không
        if (feedback.getProduct().getProductId() != productId) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        // Chỉ chính user tạo feedback mới có quyền cập nhật
        if (feedback.getUser().getId() != currentUser.getId()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Cập nhật
        if (request.getRating() != null) {
            feedback.setRating(request.getRating());
        }
        if (request.getNote() != null && !request.getNote().isEmpty()) {
            feedback.setNote(request.getNote());
        }

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        log.info("Feedback {} updated by user {}", feedbackId, currentUser.getId());

        return mapToResponse(updatedFeedback);
    }

    // Lấy tất cả feedback của user hiện tại
    public List<FeedbackResponse> getMyFeedbacks() {
        User currentUser = getCurrentAuthenticatedUser();
        List<Feedback> feedbacks = feedbackRepository.findByUserIdWithDetails(currentUser.getId());
        return feedbacks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Lấy feedback theo rating của product
    public List<FeedbackResponse> getFeedbackByProductIdAndRating(Integer productId, Integer rating) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        List<Feedback> feedbacks = feedbackRepository.findByProductIdAndRating(productId, rating);
        return feedbacks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Map entity to response
    private FeedbackResponse mapToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .userId(feedback.getUser().getId())
                .userFullName(feedback.getUser().getFullName())
                .productId(feedback.getProduct().getProductId())
                .productName(feedback.getProduct().getTitle())
                .rating(feedback.getRating())
                .note(feedback.getNote())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}

