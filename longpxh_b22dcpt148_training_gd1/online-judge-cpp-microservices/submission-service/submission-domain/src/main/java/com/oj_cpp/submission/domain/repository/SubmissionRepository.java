package com.oj_cpp.submission.domain.repository;

import com.oj_cpp.submission.domain.model.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubmissionRepository {
    Submission save(Submission submission);

    Optional<Submission> findById(Long id);

    Page<Submission> findByUserId(Long userId, Pageable pageable);
}
