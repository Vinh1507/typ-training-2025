package com.oj_cpp.judge.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {
    private Long id;
    private Long problemId;
    private String inputPath;
    private String outputPath;
}
