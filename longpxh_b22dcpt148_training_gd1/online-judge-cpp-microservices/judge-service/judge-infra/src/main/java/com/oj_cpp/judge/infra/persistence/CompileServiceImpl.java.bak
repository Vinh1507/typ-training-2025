package com.oj_cpp.judge.infra.persistence;

import com.oj_cpp.judge.domain.dto.CompileResult;
import com.oj_cpp.judge.domain.enums.LanguageJudgeEnum;
import com.oj_cpp.judge.domain.service.CompileService;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class CompileServiceImpl implements CompileService {
    @Override
    public CompileResult compile(String workDir, LanguageJudgeEnum language) {

        List<String> cmd = List.of("g++","main.cpp", "-O2","-std=c++17","-o","main");

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.directory(new File(workDir));
        processBuilder.redirectErrorStream(true);

        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String message = null;
        try {
            message = new String(process.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (exitCode == 0) {
            return new CompileResult(true, null);
        } else {
            return new CompileResult(false, message);
        }

    }
}
