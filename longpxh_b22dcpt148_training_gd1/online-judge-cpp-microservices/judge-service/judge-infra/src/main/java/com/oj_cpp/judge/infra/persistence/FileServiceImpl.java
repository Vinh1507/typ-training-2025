package com.oj_cpp.judge.infra.persistence;

import com.oj_cpp.judge.domain.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

@Repository
public class FileServiceImpl implements FileService {

    private static final String BASE_PATH = "temp/oj";

    @Override
    public String saveCodeToFile(String code, String typeFile, Long submissionId) {

        Path workDir = Paths.get(BASE_PATH, submissionId.toString());
        try {
            Files.createDirectories(workDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path pathFile = workDir.resolve("main." + typeFile);

        try {
            Files.writeString(pathFile, code, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return workDir.toAbsolutePath().toString();
    }

    @Override
    public void deleteWorkDir(String path) {

        Path workDir = Paths.get(path);
        if(!Files.exists(workDir)) return ;

        try {
            Files.walk(workDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(x -> {
                        try {
                            Files.delete(x);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
