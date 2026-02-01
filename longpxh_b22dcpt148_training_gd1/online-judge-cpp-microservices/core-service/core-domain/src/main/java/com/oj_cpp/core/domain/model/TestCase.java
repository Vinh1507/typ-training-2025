package com.oj_cpp.core.domain.model;

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
    private Boolean isHidden; // Hidden test cases not shown to students
    private Integer orderIndex; // Display order
    private Instant createdAt;
    private Instant updatedAt;
}
