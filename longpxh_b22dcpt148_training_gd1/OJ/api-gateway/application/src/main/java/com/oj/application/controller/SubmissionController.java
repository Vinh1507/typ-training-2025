package com.oj.application.controller;

import com.oj.application.dto.request.SubmitCodeRequest;
import com.oj.application.dto.response.SubmissionResponse;
import com.oj.application.security.RequestAuthContext;
import com.oj.platform.components.submission.domain.model.Submission;
import com.oj.platform.components.submission.domain.service.SubmissionService;
import com.oj.platform.core.domain.pagination.PageResult;
import com.oj.platform.core.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;
    private final RequestAuthContext authContext;

    @PostMapping
    public ApiResponse<SubmissionResponse> submit(@Valid @RequestBody SubmitCodeRequest request) {
        Long userId = authContext.currentUserId();
        if (userId == null) {
            return ApiResponse.error("User not authenticated");
        }

        Submission submission = Submission.builder()
                .userId(userId)
                .problemId(request.getProblemId())
                .code(request.getCode())
                .language(request.getLanguage())
                .status("PENDING")
                .build();

        Submission created = submissionService.submit(submission);
        SubmissionResponse response = toResponse(created);

        return ApiResponse.success(response, "Code submitted successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<SubmissionResponse> getById(@PathVariable Long id) {
        Submission submission = submissionService.findById(id);
        
        // Check if user has access to this submission
        Long userId = authContext.currentUserId();
        if (!submission.getUserId().equals(userId)) {
            return ApiResponse.error("You don't have access to this submission");
        }
        
        SubmissionResponse response = toResponse(submission);
        return ApiResponse.success(response, "Submission retrieved successfully");
    }

    @GetMapping
    public ApiResponse<List<SubmissionResponse>> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "problemId", required = false) Long problemId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("submittedAt").descending());
        Long userId = authContext.currentUserId();

        
        if (userId == null) {
            return ApiResponse.error("User not authenticated");
        }
        
        Page<Submission> submissions;
        if (problemId != null) {
            submissions = submissionService.findByProblemId(problemId, pageable);
        } else {
            submissions = submissionService.findByUserId(userId, pageable);
        }

        PageResult<SubmissionResponse> result = PageResult.of(
                submissions.getContent().stream().map(this::toResponse).toList(),
                (int) submissions.getTotalElements(),
                page,
                size
        );

        return ApiResponse.fromPage(result, "Submissions retrieved successfully");
    }

    private SubmissionResponse toResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .userId(submission.getUserId())
                .problemId(submission.getProblemId())
                .code(submission.getCode())
                .language(submission.getLanguage())
                .verdict(submission.getVerdict())
                .status(submission.getStatus())
                .executionTime(submission.getExecutionTime())
                .memoryUsed(submission.getMemoryUsed())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }
}

