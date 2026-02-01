package com.oj_cpp.core.presentation.controller;

import com.oj_cpp.core.application.dto.request.CreateTestCaseRequest;
import com.oj_cpp.core.application.dto.response.TestCaseResponse;
import com.oj_cpp.core.application.service.TestCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-cases")
@RequiredArgsConstructor
public class TestCaseController {
    private final TestCaseService testCaseService;

    @PostMapping
    public ResponseEntity<TestCaseResponse> createTestCase(@Valid @RequestBody CreateTestCaseRequest request) {
        TestCaseResponse response = testCaseService.createTestCase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCaseResponse> getTestCaseById(@PathVariable Long id) {
        TestCaseResponse response = testCaseService.getTestCaseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<TestCaseResponse>> getTestCasesByProblemId(@PathVariable Long problemId) {
        List<TestCaseResponse> responses = testCaseService.getTestCasesByProblemId(problemId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/problem/{problemId}/visible")
    public ResponseEntity<List<TestCaseResponse>> getVisibleTestCasesByProblemId(@PathVariable Long problemId) {
        List<TestCaseResponse> responses = testCaseService.getVisibleTestCasesByProblemId(problemId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        testCaseService.deleteTestCase(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/problem/{problemId}")
    public ResponseEntity<Void> deleteTestCasesByProblemId(@PathVariable Long problemId) {
        testCaseService.deleteTestCasesByProblemId(problemId);
        return ResponseEntity.noContent().build();
    }
}
