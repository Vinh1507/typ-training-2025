package com.BTL_JAVA.BTL.DTO.Response.Review;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReviewsResponse {
    Integer rating; // Rating của user
    List<ReviewDetailResponse> reviews; // List các review
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDetailResponse {
        Integer id;
        String fullName;
        String comment;
        LocalDateTime createdAt;
    }
}


