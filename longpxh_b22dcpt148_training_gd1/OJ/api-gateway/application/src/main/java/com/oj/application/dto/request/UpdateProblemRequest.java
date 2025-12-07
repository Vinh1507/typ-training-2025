package com.oj.application.dto.request;

import com.oj.platform.components.problem.infrastructure.persistence.jpa.value_object.ProblemLevelEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProblemRequest {
    private String title;
    private String content;

    @Pattern(regexp = "EASY|MEDIUM|HARD", message = "Difficulty must be EASY, MEDIUM, or HARD")
    private ProblemLevelEnum level;

    @Min(value = 100, message = "Time limit must be at least 100ms")
    private Integer timeLimit;

    @Min(value = 1024, message = "Memory limit must be at least 1024KB")
    private Integer memoryLimit;
}
