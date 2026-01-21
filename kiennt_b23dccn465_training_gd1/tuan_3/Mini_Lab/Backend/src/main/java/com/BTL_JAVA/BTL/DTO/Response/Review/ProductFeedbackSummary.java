package com.BTL_JAVA.BTL.DTO.Response.Review;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFeedbackSummary {
    Integer productId;
    String productName;
    Double averageRating;
    Long totalFeedbacks;
    Map<Integer, Long> ratingDistribution;
    List<FeedbackResponse> feedbacks;
}


