package com.oj_cpp.judge.infra.service;

import com.oj_cpp.judge.domain.dto.CompileResult;
import com.oj_cpp.judge.domain.enums.LanguageJudgeEnum;
import com.oj_cpp.judge.domain.service.CompileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CompileServiceImpl implements CompileService {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/oj_cpp/submissions/";

    @Override
    public CompileResult compile(String code, LanguageJudgeEnum language) {
        String submissionId = UUID.randomUUID().toString();
        Path submissionDir = Paths.get(TEMP_DIR + submissionId);
        
        try {
            Files.createDirectories(submissionDir);
            
            String sourceFileName = "main.cpp"; // Assuming CPP for now
            Path sourceFile = submissionDir.resolve(sourceFileName);
            Files.writeString(sourceFile, code);
            
            String outputFileName = "main";
            Path outputFile = submissionDir.resolve(outputFileName);
            
            // g++ -O2 -o main main.cpp
            ProcessBuilder pb = new ProcessBuilder(
                    "g++", "-O2", "-o", outputFile.toString(), sourceFile.toString()
            );
            pb.directory(submissionDir.toFile());
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            boolean finished = process.waitFor(10, TimeUnit.SECONDS); // 10s compile timeout
            
            if (!finished) {
                process.destroy();
                return new CompileResult(false, "Compilation timed out", null);
            }
            
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                return new CompileResult(true, "Compilation successful", outputFile.toString());
            } else {
                String errorOutput = readProcessOutput(process.getInputStream());
                return new CompileResult(false, errorOutput, null);
            }
            
        } catch (Exception e) {
            log.error("Compilation error", e);
            return new CompileResult(false, "Internal compilation error: " + e.getMessage(), null);
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
}
