package com.oj.platform.components.submission.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    private Long id;
    private Long userId;
    private Long problemId;
    private String code;
    private String language;
    private String verdict; // AC, WA, TLE, MLE, RE, CE, PENDING
    private String status; // PENDING, JUDGING, COMPLETED, FAILED
    private Integer executionTime; // in milliseconds
    private Integer memoryUsed; // in KB
    private Instant submittedAt;
}
