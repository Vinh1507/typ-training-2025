package com.oj_cpp.judge.infra.client;

import com.oj_cpp.judge.domain.dto.TestCaseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoreServiceClient {

    private final WebClient.Builder webClientBuilder;

    public List<TestCaseDTO> getTestCasesByProblemId(Long problemId) {
        log.info("Fetching test cases for problemId: {}", problemId);
        return webClientBuilder.build()
                .get()
                .uri("http://core-service/api/test-cases/problem/" + problemId)
                .retrieve()
                .bodyToFlux(TestCaseDTO.class)
                .collectList()
                .block();
    }
}
