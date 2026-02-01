package com.oj_cpp.core.application.dto.request;

import com.oj_cpp.core.domain.enums.ProblemLevelEnum;
import jakarta.validation.constraints.*;
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
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Problem level is required")
    private ProblemLevelEnum level;

    @NotNull(message = "Time limit is required")
    @Positive(message = "Time limit must be positive")
    private Integer timeLimit;

    @NotNull(message = "Memory limit is required")
    @Positive(message = "Memory limit must be positive")
    private Integer memoryLimit;

    private Long classId;
}
