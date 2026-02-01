package com.oj_cpp.submission.infra.persistence;

import com.oj_cpp.submission.domain.model.Submission;
import com.oj_cpp.submission.domain.repository.SubmissionRepository;
import com.oj_cpp.submission.infra.persistence.jpa.entity.SubmissionJpa;
import com.oj_cpp.submission.infra.persistence.jpa.mapper.SubmissionMapper;
import com.oj_cpp.submission.infra.persistence.jpa.repository.SubmissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubmissionRepositoryImpl implements SubmissionRepository {
    private final SubmissionJpaRepository jpaRepository;
    private final SubmissionMapper mapper;

    @Override
    public Submission save(Submission submission) {
        SubmissionJpa entity = mapper.toEntity(submission);
        SubmissionJpa saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Submission> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Submission> findByUserId(Long userId, Pageable pageable) {
        return jpaRepository.findByUserId(userId, pageable).map(mapper::toDomain);
    }
}
