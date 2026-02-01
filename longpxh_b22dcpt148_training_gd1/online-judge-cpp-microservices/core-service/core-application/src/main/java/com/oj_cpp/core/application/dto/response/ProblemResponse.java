package com.oj_cpp.core.application.dto.response;

import com.oj_cpp.core.domain.enums.ProblemLevelEnum;
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
    private String createdBy;
    private Long classId;
    private Instant createdAt;
    private Instant updatedAt;
}
