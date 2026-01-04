package com.BTL_JAVA.BTL.DTO.Response.Review;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackResponse {
    Integer id;
    Integer userId;
    String userFullName;
    Integer productId;
    String productName;
    Integer rating;
    String note;
    LocalDateTime createdAt;
}


