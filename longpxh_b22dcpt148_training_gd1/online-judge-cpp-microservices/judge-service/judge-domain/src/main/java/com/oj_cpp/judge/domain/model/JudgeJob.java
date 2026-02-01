package com.oj_cpp.judge.domain.model;

import com.oj_cpp.judge.domain.enums.LanguageJudgeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeJob {
    private Long submissionId;
    private Long problemId;
    private String code;
    private LanguageJudgeEnum language;
    private Integer timeLimit;
    private Integer memoryLimit;
}
