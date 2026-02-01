package com.oj_cpp.core.infra.persistence.jpa.mapper;

import com.oj_cpp.core.domain.model.TestCase;
import com.oj_cpp.core.infra.persistence.jpa.entity.TestCaseJpa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestCaseMapper {
    TestCase toDomain(TestCaseJpa entity);
    TestCaseJpa toEntity(TestCase domain);
}
