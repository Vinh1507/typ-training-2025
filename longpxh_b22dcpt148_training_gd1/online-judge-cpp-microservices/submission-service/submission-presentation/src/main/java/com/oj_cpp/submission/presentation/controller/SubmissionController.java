package com.oj_cpp.submission.presentation.controller;

import com.oj_cpp.submission.application.dto.request.SubmitCodeRequest;
import com.oj_cpp.submission.application.dto.response.SubmissionResponse;
import com.oj_cpp.submission.application.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<SubmissionResponse> submit(@Valid @RequestBody SubmitCodeRequest request, Authentication authentication) {
        Long userId = extractUserId(authentication);
        SubmissionResponse response = submissionService.submit(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponse> getById(@PathVariable Long id) {
        SubmissionResponse response = submissionService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<SubmissionResponse>> listUserSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        Pageable pageable = PageRequest.of(page, size, Sort.by("submittedAt").descending());
        Page<SubmissionResponse> responses = submissionService.getByUserId(userId, pageable);
        return ResponseEntity.ok(responses);
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            Long userId = jwt.getClaim("userId");
            if (userId != null) {
                return userId;
            }
            // Fallback or error if claim is missing (e.g. old tokens)
            // Ideally we should throw error, or try to parse subject if we were transitioning
            try {
                return Long.parseLong(jwt.getSubject());
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid user ID in token");
            }
        }
        throw new IllegalStateException("Unable to extract user ID");
    }
}
