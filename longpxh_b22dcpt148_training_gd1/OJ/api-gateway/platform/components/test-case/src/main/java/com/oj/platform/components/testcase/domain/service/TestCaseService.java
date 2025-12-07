package com.oj.platform.components.testcase.domain.service;

import com.oj.platform.components.testcase.domain.model.TestCase;
import com.oj.platform.components.testcase.infrastructure.persistence.jpa.entity.TestCaseJpa;
import com.oj.platform.components.testcase.infrastructure.persistence.jpa.repository.TestCaseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestCaseService {
    private final TestCaseJpaRepository testCaseRepository;

    @Transactional
    public TestCase create(TestCase testCase) {
        TestCaseJpa jpa = toJpa(testCase);
        TestCaseJpa saved = testCaseRepository.save(jpa);
        return toDomain(saved);
    }

    @Transactional(readOnly = true)
    public TestCase findById(Long id) {
        return testCaseRepository.findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<TestCase> findByProblemId(Long problemId) {
        return testCaseRepository.findByProblemId(problemId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestCase> findSamplesByProblemId(Long problemId) {
        return testCaseRepository.findByProblemIdAndIsSample(problemId, true).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public TestCase update(Long id, TestCase testCase) {
        TestCaseJpa existing = testCaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + id));

        existing.setInput(testCase.getInput());
        existing.setExpectedOutput(testCase.getExpectedOutput());
        existing.setIsSample(testCase.getIsSample());
        existing.setPoints(testCase.getPoints());

        TestCaseJpa updated = testCaseRepository.save(existing);
        return toDomain(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!testCaseRepository.existsById(id)) {
            throw new RuntimeException("Test case not found with id: " + id);
        }
        testCaseRepository.deleteById(id);
    }

    private TestCase toDomain(TestCaseJpa jpa) {
        return TestCase.builder()
                .id(jpa.getId())
                .problemId(jpa.getProblemId())
                .input(jpa.getInput())
                .expectedOutput(jpa.getExpectedOutput())
                .isSample(jpa.getIsSample())
                .points(jpa.getPoints())
                .createdAt(jpa.getCreatedAt())
                .build();
    }

    private TestCaseJpa toJpa(TestCase domain) {
        return TestCaseJpa.builder()
                .id(domain.getId())
                .problemId(domain.getProblemId())
                .input(domain.getInput())
                .expectedOutput(domain.getExpectedOutput())
                .isSample(domain.getIsSample())
                .points(domain.getPoints())
                .build();
    }
}
