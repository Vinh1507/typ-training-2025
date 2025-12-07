package com.oj.platform.components.submission.domain.service;

import com.oj.platform.components.submission.domain.model.Submission;
import com.oj.platform.components.submission.infrastructure.persistence.jpa.entity.SubmissionJpa;
import com.oj.platform.components.submission.infrastructure.persistence.jpa.repository.SubmissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionJpaRepository submissionRepository;

    @Transactional
    public Submission submit(Submission submission) {
        SubmissionJpa jpa = toJpa(submission);
        SubmissionJpa saved = submissionRepository.save(jpa);
        
        // TODO: Trigger async code execution
        // executorService.execute(saved.getId());
        
        return toDomain(saved);
    }

    @Transactional(readOnly = true)
    public Submission findById(Long id) {
        return submissionRepository.findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Submission> findByUserId(Long userId, Pageable pageable) {
        return submissionRepository.findByUserId(userId, pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Submission> findByProblemId(Long problemId, Pageable pageable) {
        return submissionRepository.findByProblemId(problemId, pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Submission> findByUserIdAndClassIds(Long userId, java.util.List<Long> classIds, Pageable pageable) {
        return submissionRepository.findByUserIdAndProblemClassIdIn(userId, classIds, pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Submission> findByProblemIdAndUserIdAndClassIds(Long problemId, Long userId, java.util.List<Long> classIds, Pageable pageable) {
        return submissionRepository.findByProblemIdAndUserIdAndProblemClassIdIn(problemId, userId, classIds, pageable)
                .map(this::toDomain);
    }


    @Transactional
    public Submission updateStatus(Long id, String status, String verdict, Integer executionTime, Integer memoryUsed) {
        SubmissionJpa submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + id));

        submission.setStatus(status);
        submission.setVerdict(verdict);
        submission.setExecutionTime(executionTime);
        submission.setMemoryUsed(memoryUsed);

        SubmissionJpa updated = submissionRepository.save(submission);
        return toDomain(updated);
    }

    private Submission toDomain(SubmissionJpa jpa) {
        return Submission.builder()
                .id(jpa.getId())
                .userId(jpa.getUserId())
                .problemId(jpa.getProblemId())
                .code(jpa.getCode())
                .language(jpa.getLanguage())
                .verdict(jpa.getVerdict())
                .status(jpa.getStatus())
                .executionTime(jpa.getExecutionTime())
                .memoryUsed(jpa.getMemoryUsed())
                .submittedAt(jpa.getSubmittedAt())
                .build();
    }

    private SubmissionJpa toJpa(Submission domain) {
        return SubmissionJpa.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .problemId(domain.getProblemId())
                .code(domain.getCode())
                .language(domain.getLanguage())
                .verdict(domain.getVerdict())
                .status(domain.getStatus())
                .executionTime(domain.getExecutionTime())
                .memoryUsed(domain.getMemoryUsed())
                .build();
    }
}
