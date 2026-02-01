package com.oj_cpp.core.infra.persistence.jpa.mapper;

import com.oj_cpp.core.domain.model.Problem;
import com.oj_cpp.core.infra.persistence.jpa.entity.ProblemJpa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProblemMapper {
    Problem toDomain(ProblemJpa entity);
    ProblemJpa toEntity(Problem domain);
}
