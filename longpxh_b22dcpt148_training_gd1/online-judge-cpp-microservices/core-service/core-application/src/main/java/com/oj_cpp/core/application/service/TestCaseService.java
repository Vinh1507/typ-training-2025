package com.oj_cpp.core.application.service;

import com.oj_cpp.core.application.dto.request.CreateTestCaseRequest;
import com.oj_cpp.core.application.dto.response.TestCaseResponse;
import com.oj_cpp.core.domain.model.TestCase;
import com.oj_cpp.core.domain.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;

    @Transactional
    public TestCaseResponse createTestCase(CreateTestCaseRequest request) {
        TestCase testCase = TestCase.builder()
                .problemId(request.getProblemId())
                .input(request.getInput())
                .expectedOutput(request.getExpectedOutput())
                .isHidden(request.getIsHidden())
                .orderIndex(request.getOrderIndex())
                .build();

        TestCase saved = testCaseRepository.save(testCase);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TestCaseResponse getTestCaseById(Long id) {
        TestCase testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TestCase not found with id: " + id));
        return toResponse(testCase);
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponse> getTestCasesByProblemId(Long problemId) {
        return testCaseRepository.findByProblemId(problemId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestCaseResponse> getVisibleTestCasesByProblemId(Long problemId) {
        return testCaseRepository.findByProblemIdAndIsHidden(problemId, false).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTestCase(Long id) {
        if (!testCaseRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("TestCase not found with id: " + id);
        }
        testCaseRepository.deleteById(id);
    }

    @Transactional
    public void deleteTestCasesByProblemId(Long problemId) {
        testCaseRepository.deleteByProblemId(problemId);
    }

    private TestCaseResponse toResponse(TestCase testCase) {
        return TestCaseResponse.builder()
                .id(testCase.getId())
                .problemId(testCase.getProblemId())
                .input(testCase.getInput())
                .expectedOutput(testCase.getExpectedOutput())
                .isHidden(testCase.getIsHidden())
                .orderIndex(testCase.getOrderIndex())
                .createdAt(testCase.getCreatedAt())
                .updatedAt(testCase.getUpdatedAt())
                .build();
    }
}
