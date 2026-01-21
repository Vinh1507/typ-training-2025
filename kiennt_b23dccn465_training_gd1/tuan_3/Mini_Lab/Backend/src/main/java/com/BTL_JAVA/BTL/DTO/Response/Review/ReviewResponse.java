package com.BTL_JAVA.BTL.DTO.Response.Review;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReviewResponse {
    Integer id;
    String fullName;
    Integer rating;
    String comment;
    LocalDateTime createdAt;
}

