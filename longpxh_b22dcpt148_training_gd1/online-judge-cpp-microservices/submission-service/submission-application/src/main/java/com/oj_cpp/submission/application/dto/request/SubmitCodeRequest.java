package com.oj_cpp.submission.application.dto.request;

import com.oj_cpp.submission.domain.enums.LanguageEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitCodeRequest {
    @NotNull(message = "Problem ID is required")
    private Long problemId;
    
    @NotBlank(message = "Code is required")
    private String code;
    
    @NotNull(message = "Language is required")
    private LanguageEnum language;
}
