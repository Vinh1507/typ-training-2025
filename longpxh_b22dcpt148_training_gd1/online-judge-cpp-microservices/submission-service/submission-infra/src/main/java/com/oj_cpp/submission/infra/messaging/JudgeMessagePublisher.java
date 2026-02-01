package com.oj_cpp.submission.infra.messaging;

import com.oj_cpp.submission.domain.service.JudgeMessagePublisherPort;
import com.oj_cpp.submission.infra.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JudgeMessagePublisher implements JudgeMessagePublisherPort {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publishes a submission to the judge queue for processing
     * @param submissionId The ID of the submission to judge
     * @param problemId The ID of the problem being submitted
     * @param code The source code to judge
     * @param language The programming language (e.g., "CPP")
     */
    public void publishJudgeRequest(Long submissionId, Long problemId, String code, String language) {
        Map<String, Object> message = new HashMap<>();
        message.put("submissionId", submissionId);
        message.put("problemId", problemId);
        message.put("code", code);
        message.put("language", language);

        log.info("Publishing judge request for submission {}", submissionId);
        
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.JUDGE_EXCHANGE,
                RabbitMQConfig.JUDGE_ROUTING_KEY,
                message
        );
        
        log.info("Successfully published judge request for submission {}", submissionId);
    }
}
