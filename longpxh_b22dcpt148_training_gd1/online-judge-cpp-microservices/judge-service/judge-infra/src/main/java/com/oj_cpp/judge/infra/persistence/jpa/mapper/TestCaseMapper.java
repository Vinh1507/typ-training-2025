package com.oj_cpp.judge.infra.persistence.jpa.mapper;

import com.oj_cpp.judge.domain.model.TestCase;
import com.oj_cpp.judge.infra.persistence.jpa.entity.TestCaseJpa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestCaseMapper {

    TestCaseJpa toEntity(TestCase testCase);
    TestCase toDomain(TestCaseJpa testCaseJpa);

}
