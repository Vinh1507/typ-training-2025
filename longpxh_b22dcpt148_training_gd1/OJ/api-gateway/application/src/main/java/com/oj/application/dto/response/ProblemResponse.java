package com.oj.application.dto.response;

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
public class ProblemResponse {
    private Long id;
    private String problemCode;
    private String title;
    private String content;
    private ProblemLevelEnum level;
    private Integer timeLimit;
    private Integer memoryLimit;
    private Long createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
