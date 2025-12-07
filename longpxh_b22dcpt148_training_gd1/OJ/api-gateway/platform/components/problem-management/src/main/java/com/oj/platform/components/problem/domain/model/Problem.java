package com.oj.platform.components.problem.domain.model;

import com.oj.platform.components.problem.infrastructure.persistence.jpa.value_object.ProblemLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    private Long id;
    private String problemCode;
    private String title;
    private String content;
    private ProblemLevelEnum level; // EASY, MEDIUM, HARD
    private Integer timeLimit; // in milliseconds
    private Integer memoryLimit; // in MB
    private Long createdBy;
    private Long classId; // Class this problem belongs to
    private Instant createdAt;
    private Instant updatedAt;
}
