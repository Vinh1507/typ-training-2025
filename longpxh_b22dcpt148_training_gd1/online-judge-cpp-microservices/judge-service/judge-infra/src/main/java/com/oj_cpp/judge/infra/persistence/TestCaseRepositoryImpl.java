package com.oj_cpp.judge.infra.persistence;

import com.oj_cpp.judge.domain.model.TestCase;
import com.oj_cpp.judge.domain.repository.TestCaseRepository;
import com.oj_cpp.judge.infra.persistence.jpa.entity.TestCaseJpa;
import com.oj_cpp.judge.infra.persistence.jpa.mapper.TestCaseMapper;
import com.oj_cpp.judge.infra.persistence.jpa.repository.TestCaseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TestCaseRepositoryImpl implements TestCaseRepository {

    private final TestCaseJpaRepository testCaseJpaRepository;
    private final TestCaseMapper mapper;

    @Override
    public List<TestCase> findByProblemId(Long problemId) {

        List<TestCaseJpa> testCaseJpaList = testCaseJpaRepository.findByProblemId(problemId);
        List<TestCase> testCaseList = new ArrayList<>();
        for(TestCaseJpa testCaseJpa : testCaseJpaList){
            testCaseList.add(mapper.toDomain(testCaseJpa));
        }
        return  testCaseList;
    }
}
