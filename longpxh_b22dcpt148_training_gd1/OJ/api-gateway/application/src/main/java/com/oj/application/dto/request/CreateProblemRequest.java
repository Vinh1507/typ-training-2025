package com.oj.application.dto.request;

import com.oj.platform.components.problem.infrastructure.persistence.jpa.value_object.ProblemLevelEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProblemRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "level is required")
    @Pattern(regexp = "EASY|MEDIUM|HARD", message = "Difficulty must be EASY, MEDIUM, or HARD")
    private ProblemLevelEnum level;

    @NotNull(message = "Time limit is required")
    @Min(value = 100, message = "Time limit must be at least 100ms")
    private Integer timeLimit;

    @NotNull(message = "Memory limit is required")
    @Min(value = 1024, message = "Memory limit must be at least 1024KB")
    private Integer memoryLimit;
}
