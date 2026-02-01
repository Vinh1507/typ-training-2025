package com.oj_cpp.submission.application.dto.response;

import com.oj_cpp.submission.domain.enums.JudgeResultEnum;
import com.oj_cpp.submission.domain.enums.LanguageEnum;
import com.oj_cpp.submission.domain.enums.SubmissionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {
    private Long id;
    private Long userId;
    private Long problemId;
    private String code;
    private LanguageEnum language;
    private JudgeResultEnum judgeResult;
    private SubmissionStatusEnum status;
    private Integer executionTime;
    private Integer memoryUsed;
    private Instant submittedAt;
}
