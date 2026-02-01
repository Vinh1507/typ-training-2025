package com.oj_cpp.judge.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDTO {
    private Long id;
    private Long problemId;
    private String input;
    private String expectedOutput;
    private Boolean isHidden;
    private Integer orderIndex;
}
