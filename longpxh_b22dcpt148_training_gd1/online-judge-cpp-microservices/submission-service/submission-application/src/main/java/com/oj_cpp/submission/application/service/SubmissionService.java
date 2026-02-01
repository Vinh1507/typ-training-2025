package com.oj_cpp.submission.application.service;

import com.oj_cpp.submission.application.dto.request.SubmitCodeRequest;
import com.oj_cpp.submission.application.dto.response.SubmissionResponse;
import com.oj_cpp.submission.domain.enums.SubmissionStatusEnum;
import com.oj_cpp.submission.domain.model.Submission;
import com.oj_cpp.submission.domain.repository.SubmissionRepository;
import com.oj_cpp.submission.domain.service.JudgeMessagePublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final JudgeMessagePublisherPort judgeMessagePublisher;

    @Transactional
    public SubmissionResponse submit(SubmitCodeRequest request, Long userId) {
        Submission submission = Submission.builder()
                .userId(userId)
                .problemId(request.getProblemId())
                .code(request.getCode())
                .language(request.getLanguage())
                .status(SubmissionStatusEnum.PENDING)
                .build();

        Submission saved = submissionRepository.save(submission);
        
        // Publish to judge queue
        try {
            judgeMessagePublisher.publishJudgeRequest(
                saved.getId(),
                saved.getProblemId(),
                saved.getCode(),
                saved.getLanguage().name()
            );
            log.info("Submitted code for judging: submissionId={}", saved.getId());
        } catch (Exception e) {
            log.error("Failed to publish judge request for submissionId={}: {}", saved.getId(), e.getMessage());
            // Don't fail the submission, the judge can be retried
        }
        
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SubmissionResponse getById(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
        return toResponse(submission);
    }

    @Transactional(readOnly = true)
    public Page<SubmissionResponse> getByUserId(Long userId, Pageable pageable) {
        return submissionRepository.findByUserId(userId, pageable)
                .map(this::toResponse);
    }

    private SubmissionResponse toResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .userId(submission.getUserId())
                .problemId(submission.getProblemId())
                .code(submission.getCode())
                .language(submission.getLanguage())
                .judgeResult(submission.getJudgeResult())
                .status(submission.getStatus())
                .executionTime(submission.getExecutionTime())
                .memoryUsed(submission.getMemoryUsed())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }
}
