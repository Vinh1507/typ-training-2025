package com.oj.platform.components.testcase.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {
    private Long id;
    private Long problemId;
    private String input;
    private String expectedOutput;
    private Boolean isSample;
    private Integer points;
    private Instant createdAt;
}
