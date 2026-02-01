package com.oj_cpp.core.infra.persistence;

import com.oj_cpp.core.domain.model.TestCase;
import com.oj_cpp.core.domain.repository.TestCaseRepository;
import com.oj_cpp.core.infra.persistence.jpa.entity.TestCaseJpa;
import com.oj_cpp.core.infra.persistence.jpa.mapper.TestCaseMapper;
import com.oj_cpp.core.infra.persistence.jpa.repository.TestCaseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TestCaseRepositoryImpl implements TestCaseRepository {
    private final TestCaseJpaRepository testCaseJpaRepository;
    private final TestCaseMapper testCaseMapper;

    @Override
    public TestCase save(TestCase testCase) {
        TestCaseJpa entity = testCaseMapper.toEntity(testCase);
        TestCaseJpa saved = testCaseJpaRepository.save(entity);
        return testCaseMapper.toDomain(saved);
    }

    @Override
    public Optional<TestCase> findById(Long id) {
        return testCaseJpaRepository.findById(id)
                .map(testCaseMapper::toDomain);
    }

    @Override
    public List<TestCase> findByProblemId(Long problemId) {
        return testCaseJpaRepository.findByProblemId(problemId).stream()
                .map(testCaseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestCase> findByProblemIdAndIsHidden(Long problemId, Boolean isHidden) {
        return testCaseJpaRepository.findByProblemIdAndIsHidden(problemId, isHidden).stream()
                .map(testCaseMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        testCaseJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByProblemId(Long problemId) {
        testCaseJpaRepository.deleteByProblemId(problemId);
    }
}
