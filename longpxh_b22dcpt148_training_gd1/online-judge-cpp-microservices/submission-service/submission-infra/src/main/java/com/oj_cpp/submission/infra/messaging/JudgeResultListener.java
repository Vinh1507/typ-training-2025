package com.oj_cpp.submission.infra.messaging;

import com.oj_cpp.submission.domain.enums.JudgeResultEnum;
import com.oj_cpp.submission.domain.enums.SubmissionStatusEnum;
import com.oj_cpp.submission.domain.model.Submission;
import com.oj_cpp.submission.domain.repository.SubmissionRepository;
import com.oj_cpp.submission.infra.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JudgeResultListener {

    private final SubmissionRepository submissionRepository;

    @RabbitListener(queues = RabbitMQConfig.RESULT_QUEUE)
    @Transactional
    public void handleJudgeResult(Map<String, Object> message) {
        Long submissionId = ((Number) message.get("submissionId")).longValue();
        String judgeResultStr = (String) message.get("judgeResult");
        Long executionTime = message.get("executionTime") != null ? 
                ((Number) message.get("executionTime")).longValue() : null;
        Long memoryUsed = message.get("memoryUsed") != null ? 
                ((Number) message.get("memoryUsed")).longValue() : null;
        String errorMessage = (String) message.get("errorMessage");

        log.info("Received judge result: submissionId={}, result={}", submissionId, judgeResultStr);

        try {
            Submission submission = submissionRepository.findById(submissionId)
                    .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));

            // Convert String to Enum
            if (judgeResultStr != null) {
                try {
                    submission.setJudgeResult(JudgeResultEnum.valueOf(judgeResultStr));
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid judge result enum: {}", judgeResultStr);
                }
            }
            
            // Convert Long to Integer
            submission.setExecutionTime(executionTime != null ? executionTime.intValue() : null);
            submission.setMemoryUsed(memoryUsed != null ? memoryUsed.intValue() : null);
            submission.setStatus(SubmissionStatusEnum.COMPLETED);

            submissionRepository.save(submission);
            submission.setStatus(SubmissionStatusEnum.COMPLETED);

            submissionRepository.save(submission);
            
            log.info("Updated submission {} with result {}", submissionId, judgeResultStr);
            
        } catch (Exception e) {
            log.error("Failed to update submission {}: {}", submissionId, e.getMessage());
        }
    }
}
