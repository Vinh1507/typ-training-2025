package com.oj_cpp.core.presentation.controller;

import com.oj_cpp.core.application.dto.request.CreateProblemRequest;
import com.oj_cpp.core.application.dto.response.ProblemResponse;
import com.oj_cpp.core.application.service.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;
    private final com.oj_cpp.core.application.service.TestCaseService testCaseService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProblemResponse> createProblem(
            @RequestPart(value = "data", required = false) @Valid CreateProblemRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication) {
        
        String username = extractUsername(authentication);
        try {
            InputStream fileStream = (file != null) ? file.getInputStream() : null;
            ProblemResponse response = problemService.createOrImport(request, fileStream, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process request", e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> getProblemById(@PathVariable Long id) {
        ProblemResponse response = problemService.getProblemById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProblemResponse>> getAllProblems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) Long classId
    ) {
        List<ProblemResponse> responses = problemService.getAllProblems(keyword, code, title, level, classId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/test-cases")
    public ResponseEntity<List<com.oj_cpp.core.application.dto.response.TestCaseResponse>> getTestCases(@PathVariable Long id) {
        List<com.oj_cpp.core.application.dto.response.TestCaseResponse> testCases = 
            testCaseService.getVisibleTestCasesByProblemId(id);
        return ResponseEntity.ok(testCases);
    }

    private String extractUsername(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        throw new IllegalStateException("Unable to extract username from authentication");
    }
}
