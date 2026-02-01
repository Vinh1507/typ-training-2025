package com.oj_cpp.core.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTestCaseRequest {
    @NotNull(message = "Problem ID is required")
    private Long problemId;

    @NotBlank(message = "Input is required")
    private String input;

    @NotBlank(message = "Expected output is required")
    private String expectedOutput;

    @NotNull(message = "isHidden flag is required")
    private Boolean isHidden;

    @NotNull(message = "Order index is required")
    @PositiveOrZero(message = "Order index must be >= 0")
    private Integer orderIndex;
}
