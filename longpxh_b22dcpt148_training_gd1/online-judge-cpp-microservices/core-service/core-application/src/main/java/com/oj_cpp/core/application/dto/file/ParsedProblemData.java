package com.oj_cpp.core.application.dto.file;

import com.oj_cpp.core.application.dto.request.CreateProblemRequest;
import com.oj_cpp.core.domain.model.TestCase;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ParsedProblemData {
    private CreateProblemRequest request;
    private List<TestCase> testCases;
}
