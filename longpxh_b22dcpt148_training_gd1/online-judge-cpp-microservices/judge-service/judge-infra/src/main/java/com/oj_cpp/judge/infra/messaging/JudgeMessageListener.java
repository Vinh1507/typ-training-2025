package com.oj_cpp.judge.infra.messaging;

import com.oj_cpp.judge.domain.dto.CompileResult;
import com.oj_cpp.judge.domain.dto.TestCaseDTO;
import com.oj_cpp.judge.domain.enums.LanguageJudgeEnum;
import com.oj_cpp.judge.domain.service.CompileService;
import com.oj_cpp.judge.infra.client.CoreServiceClient;
import com.oj_cpp.judge.infra.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class JudgeMessageListener {

    private final RabbitTemplate rabbitTemplate;
    private final CoreServiceClient coreServiceClient;
    private final CompileService compileService;

    @RabbitListener(queues = RabbitMQConfig.JUDGE_QUEUE)
    public void handleJudgeRequest(Map<String, Object> message) {
        Long submissionId = ((Number) message.get("submissionId")).longValue();
        Long problemId = ((Number) message.get("problemId")).longValue();
        String code = (String) message.get("code");
        String languageStr = (String) message.get("language");
        
        LanguageJudgeEnum language;
        try {
             language = LanguageJudgeEnum.valueOf(languageStr);
        } catch (Exception e) {
             language = LanguageJudgeEnum.CPP;
        }

        log.info("Received judge request: submissionId={}, problemId={}, language={}", 
                submissionId, problemId, language);

        try {
            // Fetch test cases
            List<TestCaseDTO> testCases = coreServiceClient.getTestCasesByProblemId(problemId);
            if (testCases == null || testCases.isEmpty()) {
                publishResult(submissionId, "CE", 0L, 0L, "No test cases found for problem");
                return;
            }

            // Compile code
            CompileResult compileResult = compileService.compile(code, language);
            if (!compileResult.getSuccess()) {
                publishResult(submissionId, "CE", 0L, 0L, compileResult.getMessage());
                return;
            }
            
            String executablePath = compileResult.getExecutablePath();
            
    
            long maxTime = 0;
            long maxMemory = 0;
            String finalResult = "AC";
            
            for (TestCaseDTO testCase : testCases) {
                RunResult runResult = runTestCase(executablePath, testCase.getInput(), testCase.getExpectedOutput());
                
                if (runResult.time > maxTime) maxTime = runResult.time;
                if (runResult.memory > maxMemory) maxMemory = runResult.memory;
                
                if (!runResult.status.equals("AC")) {
                    finalResult = runResult.status;
                    break;
                }
            }
            
            try {
                Files.deleteIfExists(Paths.get(executablePath));
                Files.deleteIfExists(Paths.get(executablePath).getParent());
            } catch (Exception e) {
                log.warn("Failed to cleanup temp files: {}", e.getMessage());
            }
            publishResult(submissionId, finalResult, maxTime, maxMemory, null);
            
            log.info("Judge completed for submissionId={}, result={}", submissionId, finalResult);
            
        } catch (Exception e) {
            log.error("Failed to judge submissionId={}: {}", submissionId, e.getMessage());
            publishResult(submissionId, "IE", 0L, 0L, e.getMessage()); // Internal Error
        }
    }
    
    @Data
    @AllArgsConstructor
    private static class RunResult {
        String status;
        long time;
        long memory;
    }

    private RunResult runTestCase(String executablePath, String input, String expectedOutput) {
        try {
            ProcessBuilder pb = new ProcessBuilder(executablePath);
            long startTime = System.currentTimeMillis();
            
            Process process = pb.start();
      
            if (input != null && !input.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                    writer.write(input);
                    writer.flush();
                }
            }
            
            boolean finished = process.waitFor(2, TimeUnit.SECONDS); 
            long timeTaken = System.currentTimeMillis() - startTime;
            
            if (!finished) {
                process.destroyForcibly();
                return new RunResult("TLE", 2000, 0);
            }
            
            if (process.exitValue() != 0) {
                return new RunResult("RTE", timeTaken, 0);
            }
            
            // Read output
            String output = readProcessOutput(process.getInputStream());
 
            String normalizedOutput = output.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
            String normalizedExpected = expectedOutput.trim().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
            
            if (normalizedOutput.equals(normalizedExpected)) {
                 return new RunResult("AC", timeTaken, 0); 
            } else {
                 return new RunResult("WA", timeTaken, 0);
            }
            
        } catch (Exception e) {
            log.error("Error executing test case", e);
            return new RunResult("IE", 0, 0);
        }
    }

    private String readProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    private void publishResult(Long submissionId, String judgeResult, 
                               Long executionTime, Long memoryUsed, String errorMessage) {
        Map<String, Object> result = new HashMap<>();
        result.put("submissionId", submissionId);
        result.put("judgeResult", judgeResult);
        result.put("executionTime", executionTime);
        result.put("memoryUsed", memoryUsed);
        result.put("errorMessage", errorMessage);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.JUDGE_EXCHANGE,
                RabbitMQConfig.RESULT_ROUTING_KEY,
                result
        );
    }
}
