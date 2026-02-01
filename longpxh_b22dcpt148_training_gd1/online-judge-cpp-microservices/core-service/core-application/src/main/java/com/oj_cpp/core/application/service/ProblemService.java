package com.oj_cpp.core.application.service;

import com.oj_cpp.core.application.dto.request.CreateProblemRequest;
import com.oj_cpp.core.application.dto.response.ProblemResponse;
import com.oj_cpp.core.domain.model.Problem;
import com.oj_cpp.core.domain.model.TestCase;
import com.oj_cpp.core.domain.repository.ProblemRepository;
import com.oj_cpp.core.domain.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.oj_cpp.core.application.dto.file.ParsedProblemData;
import com.oj_cpp.core.application.service.file.PolygonZipParser;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;

    @Transactional
    public ProblemResponse createProblem(CreateProblemRequest request, String username) {
        String generatedCode = generateProblemCode();


        Problem problem = Problem.builder()
                .problemCode(generatedCode)
                .title(request.getTitle())
                .content(request.getContent())
                .level(request.getLevel())
                .timeLimit(request.getTimeLimit())
                .memoryLimit(request.getMemoryLimit())
                .classId(request.getClassId())
                .createdBy(username)
                .build();

        Problem saved = problemRepository.save(problem);
        return toResponse(saved);
    }

    @Transactional
    public ProblemResponse createProblemWithTestCases(CreateProblemRequest request, List<TestCase> testCases, String username) {
        ProblemResponse problemResponse = createProblem(request, username);
        Long problemId = problemResponse.getId();

        if (testCases != null) {
            for (TestCase tc : testCases) {
                tc.setProblemId(problemId);
                testCaseRepository.save(tc);
            }
        }

        return problemResponse;
    }

    private String generateProblemCode() {
        return problemRepository.findTopByOrderByProblemCodeDesc()
                .map(lastProblem -> {
                    String lastCode = lastProblem.getProblemCode();
                    if (lastCode.startsWith("CPP")) {
                        try {
                            int number = Integer.parseInt(lastCode.substring(3));
                            return String.format("CPP%03d", number + 1);
                        } catch (NumberFormatException e) {
                            return "CPP001";
                        }
                    }
                    return "CPP001";
                })
                .orElse("CPP001");
    }

    @Transactional
    public ProblemResponse createOrImport(CreateProblemRequest request, InputStream fileStream, String username) {
        try {
            if (request == null && fileStream != null) {
                ParsedProblemData data = PolygonZipParser.parse(fileStream);
                CreateProblemRequest importedRequest = data.getRequest();
                // If importing just zip, ensure default level or other fields if missing
                return createProblemWithTestCases(importedRequest, data.getTestCases(), username);
            }
            
            if (request != null) {
                List<TestCase> testCases = new ArrayList<>();
                if (fileStream != null) {
                    ParsedProblemData data = PolygonZipParser.parse(fileStream);
                    CreateProblemRequest importedRequest = data.getRequest();
                    
                    // Merge the data and request: JSON data from 'data' part + testcases from zip
                    if (importedRequest.getTitle() == null) {
                        importedRequest.setTitle(request.getTitle());
                    }
                    if (importedRequest.getContent() == null) {
                        importedRequest.setContent(request.getContent());
                    }
                    if (importedRequest.getLevel() == null) {
                        importedRequest.setLevel(request.getLevel());
                    }
                    if (importedRequest.getTimeLimit() == null) {
                        importedRequest.setTimeLimit(request.getTimeLimit());
                    }
                    if (importedRequest.getMemoryLimit() == null) {
                        importedRequest.setMemoryLimit(request.getMemoryLimit());
                    }
                    if (importedRequest.getClassId() == null) {
                        importedRequest.setClassId(request.getClassId());
                    }
                    
                    return createProblemWithTestCases(importedRequest, data.getTestCases(), username);
                } else {
                    return createProblem(request, username);
                }
            }
            
            throw new IllegalArgumentException("Either request data or file must be provided");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create/import problem", e);
        }
    }

    private List<TestCase> extractSimpleTestCases(InputStream inputStream) throws java.io.IOException {
        Map<String, String> fileContentMap = new HashMap<>();

        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (!zipEntry.isDirectory()) {
                    String fileName = zipEntry.getName();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int nRead;
                    while ((nRead = zis.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    fileContentMap.put(fileName, buffer.toString(StandardCharsets.UTF_8));
                }
                zipEntry = zis.getNextEntry();
            }
        }

        List<TestCase> testCases = new ArrayList<>();
        int order = 1;

        List<String> sortedKeys = new ArrayList<>(fileContentMap.keySet());
        Collections.sort(sortedKeys);

        for (String fileName : sortedKeys) {
            if (fileName.endsWith(".in")) {
                String baseName = fileName.substring(0, fileName.length() - 3);
                String outName = baseName + ".out";

                if (fileContentMap.containsKey(outName)) {
                    testCases.add(TestCase.builder()
                            .input(fileContentMap.get(fileName))
                            .expectedOutput(fileContentMap.get(outName))
                            .isHidden(false)
                            .orderIndex(order++)
                            .build());
                }
            }
        }

        return testCases;
    }

    @Transactional(readOnly = true)
    public ProblemResponse getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + id));
        return toResponse(problem);
    }

    @Transactional(readOnly = true)
    public ProblemResponse getProblemByCode(String problemCode) {
        Problem problem = problemRepository.findByProblemCode(problemCode)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with code: " + problemCode));
        return toResponse(problem);
    }

    @Transactional(readOnly = true)
    public List<ProblemResponse> getAllProblems(String keyword, String code, String title, String level, Long classId) {
        return problemRepository.search(keyword, code, title, level, classId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteProblem(Long id) {
        if (!problemRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Problem not found with id: " + id);
        }
        problemRepository.deleteById(id);
    }

    private ProblemResponse toResponse(Problem problem) {
        return ProblemResponse.builder()
                .id(problem.getId())
                .problemCode(problem.getProblemCode())
                .title(problem.getTitle())
                .content(problem.getContent())
                .level(problem.getLevel())
                .timeLimit(problem.getTimeLimit())
                .memoryLimit(problem.getMemoryLimit())
                .createdBy(problem.getCreatedBy())
                .classId(problem.getClassId())
                .createdAt(problem.getCreatedAt())
                .updatedAt(problem.getUpdatedAt())
                .build();
    }
}
