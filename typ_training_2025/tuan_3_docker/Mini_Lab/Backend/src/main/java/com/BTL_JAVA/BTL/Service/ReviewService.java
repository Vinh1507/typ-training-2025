package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.Review.ReviewRequest;
import com.BTL_JAVA.BTL.DTO.Response.Review.ReviewResponse;
import com.BTL_JAVA.BTL.DTO.Response.Review.UserReviewsResponse;
import com.BTL_JAVA.BTL.Entity.Review;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.ReviewRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReviewService {
    final ReviewRepository reviewRepository;
    final UserRepository userRepository;


    // tạo mới review
    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByFullName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Nếu có rating thì cập nhật
        if (request.getRating() != null) {
            user.setRating(request.getRating());
        }

        if (request.getComment() == null) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        Review review = Review.builder()
                .user(user)
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToResponse(savedReview);
    }

    // Cập nhật review - Tối ưu với JOIN FETCH
    @Transactional
    public ReviewResponse updateReview(Integer reviewId, ReviewRequest request) {
        Review review = reviewRepository.findByIdWithUser(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!review.getUser().getFullName().equals(username)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        review.setComment(request.getComment());
        if (request.getRating() != null) {
            review.getUser().setRating(request.getRating());
        }

        if (request.getComment() == null) {
            review.setComment(review.getComment());
        }

        Review updatedReview = reviewRepository.save(review);
        return mapToResponse(updatedReview);
    }

    // Lấy tất cả review - Tối ưu với JOIN FETCH
    public List<ReviewResponse> getAllReview() {
        return reviewRepository.findAllWithUser().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Xóa review - Tối ưu với JOIN FETCH
    @Transactional
    public void deleteReview(Integer reviewId) {
        Review review = reviewRepository.findByIdWithUser(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByFullName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getNameRoles().equals("ADMIN"));

        if (!isAdmin && review.getUser().getId() != currentUser.getId()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        reviewRepository.delete(review);
    }

    // Lấy reviews theo user - Format đặc biệt với rating ở ngoài
    public UserReviewsResponse getReviewByUserId(Integer userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByFullName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Review> reviews = reviewRepository.findByUserIdWithUser(userId);
        
        // Nếu không có review nào, trả về rating null
        Integer userRating = reviews.isEmpty() ? null : reviews.get(0).getUser().getRating();
        
        List<UserReviewsResponse.ReviewDetailResponse> reviewDetails = reviews.stream()
                .map(review -> UserReviewsResponse.ReviewDetailResponse.builder()
                        .id(review.getId())
                        .fullName(review.getUser().getFullName())
                        .comment(review.getComment())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return UserReviewsResponse.builder()
                .rating(userRating)
                .reviews(reviewDetails)
                .build();
    }
    
    // Phân trang cho tất cả reviews - Tối ưu cho dữ liệu lớn
    public Page<ReviewResponse> getAllReviewsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findAllWithUserPaginated(pageable);
        return reviewPage.map(this::mapToResponse);
    }
    
    // Phân trang cho reviews của user cụ thể
    public Page<ReviewResponse> getReviewsByUserIdPaginated(int userId, int page, int size) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByFullName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (currentUser.getId() != userId) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findByUserId(userId, pageable);
        return reviewPage.map(this::mapToResponse);
    }
    
    // Lấy reviews theo rating (chỉ lấy reviews có rating)
    public List<ReviewResponse> getReviewsByRating(Integer rating) {
        // Validate rating từ 1-5
        if (rating < 1 || rating > 5) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        return reviewRepository.findByRatingWithUser(rating).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    // Lấy reviews có rating >= giá trị cho trước
    public List<ReviewResponse> getReviewsByMinRating(Integer minRating) {
        // Validate rating từ 1-5
        if (minRating < 1 || minRating > 5) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        return reviewRepository.findByRatingGreaterThanEqualWithUser(minRating).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    // Lấy tất cả reviews có rating (không null)
    public List<ReviewResponse> getAllReviewsWithRating() {
        return reviewRepository.findAllWithRating().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    // Lấy reviews không có rating (null) - có thể là reviews cũ
    public List<ReviewResponse> getAllReviewsWithoutRating() {
        return reviewRepository.findAllWithoutRating().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .fullName(review.getUser().getFullName())
                .rating(review.getUser().getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
