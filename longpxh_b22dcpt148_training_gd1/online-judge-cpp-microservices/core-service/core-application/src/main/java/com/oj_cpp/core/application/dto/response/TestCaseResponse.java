package com.oj_cpp.core.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResponse {
    private Long id;
    private Long problemId;
    private String input;
    private String expectedOutput;
    private Boolean isHidden;
    private Integer orderIndex;
    private Instant createdAt;
    private Instant updatedAt;
}
