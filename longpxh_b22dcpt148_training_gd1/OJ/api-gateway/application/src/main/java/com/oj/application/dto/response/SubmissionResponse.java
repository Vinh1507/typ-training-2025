package com.oj.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {
    private Long id;
    private Long userId;
    private Long problemId;
    private String code;
    private String language;
    private String verdict;
    private String status;
    private Integer executionTime;
    private Integer memoryUsed;
    private Instant submittedAt;
}
