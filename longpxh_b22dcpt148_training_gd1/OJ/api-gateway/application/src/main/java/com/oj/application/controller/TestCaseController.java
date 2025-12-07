package com.oj.application.controller;

import com.oj.application.dto.request.CreateTestCaseRequest;
import com.oj.application.dto.response.TestCaseResponse;
import com.oj.platform.components.testcase.domain.model.TestCase;
import com.oj.platform.components.testcase.domain.service.TestCaseService;
import com.oj.platform.core.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test-cases")
@RequiredArgsConstructor
public class TestCaseController {
    private final TestCaseService testCaseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TestCaseResponse> create(@Valid @RequestBody CreateTestCaseRequest request) {

        TestCase testCase = TestCase.builder()
                .problemId(request.getProblemId())
                .input(request.getInput())
                .expectedOutput(request.getExpectedOutput())
                .isSample(request.getIsSample())
                .points(request.getPoints())
                .build();

        TestCase created = testCaseService.create(testCase);
        TestCaseResponse response = toResponse(created);

        return ApiResponse.success(response, "Test case created successfully");
    }

    @GetMapping("/problem/{problemId}")
    public ApiResponse<List<TestCaseResponse>> getByProblemId(
            @PathVariable("problemId") Long problemId,
            @RequestParam(name = "samplesOnly", required = false) Boolean samplesOnly) {

        List<TestCase> testCases;
        if (Boolean.TRUE.equals(samplesOnly)) {
            testCases = testCaseService.findSamplesByProblemId(problemId);
        } else {
            testCases = testCaseService.findByProblemId(problemId);
        }

        List<TestCaseResponse> responses = testCases.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.success(responses, "Test cases retrieved successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TestCaseResponse> getById(@PathVariable("id") Long id) {
        TestCase testCase = testCaseService.findById(id);
        TestCaseResponse response = toResponse(testCase);
        return ApiResponse.success(response, "Test case retrieved successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TestCaseResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateTestCaseRequest request) {

        TestCase testCase = TestCase.builder()
                .input(request.getInput())
                .expectedOutput(request.getExpectedOutput())
                .isSample(request.getIsSample())
                .points(request.getPoints())
                .build();

        TestCase updated = testCaseService.update(id, testCase);
        TestCaseResponse response = toResponse(updated);

        return ApiResponse.success(response, "Test case updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        testCaseService.delete(id);
        return ApiResponse.success(null, "Test case deleted successfully");
    }

    private TestCaseResponse toResponse(TestCase testCase) {
        return TestCaseResponse.builder()
                .id(testCase.getId())
                .problemId(testCase.getProblemId())
                .input(testCase.getInput())
                .expectedOutput(testCase.getExpectedOutput())
                .isSample(testCase.getIsSample())
                .points(testCase.getPoints())
                .createdAt(testCase.getCreatedAt())
                .build();
    }
}
