package com.oj_cpp.judge.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompileResult {
    private Boolean success;
    private String message;
    private String executablePath;
}
