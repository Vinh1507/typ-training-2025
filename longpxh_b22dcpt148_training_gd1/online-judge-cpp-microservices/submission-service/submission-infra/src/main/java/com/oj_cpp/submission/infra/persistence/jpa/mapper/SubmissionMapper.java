package com.oj_cpp.submission.infra.persistence.jpa.mapper;

import com.oj_cpp.submission.domain.model.Submission;
import com.oj_cpp.submission.infra.persistence.jpa.entity.SubmissionJpa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubmissionMapper {
    Submission toDomain(SubmissionJpa jpa);
    
    SubmissionJpa toEntity(Submission domain);
}
