package com.oj_cpp.submission.domain.service;

/**
 * Interface for publishing submissions to the judge service
 * Implementation is in the infra layer using message queue
 */
public interface JudgeMessagePublisherPort {
    
    /**
     * Publishes a submission to the judge queue for processing
     * @param submissionId The ID of the submission to judge
     * @param problemId The ID of the problem being submitted
     * @param code The source code to judge
     * @param language The programming language (e.g., "CPP")
     */
    void publishJudgeRequest(Long submissionId, Long problemId, String code, String language);
}
