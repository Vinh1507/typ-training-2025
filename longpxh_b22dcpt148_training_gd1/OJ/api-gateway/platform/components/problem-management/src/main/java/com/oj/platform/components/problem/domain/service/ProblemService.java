package com.oj.platform.components.problem.domain.service;

import com.oj.platform.components.problem.domain.model.Problem;
import com.oj.platform.components.problem.infrastructure.persistence.jpa.entity.ProblemJpa;
import com.oj.platform.components.problem.infrastructure.persistence.jpa.repository.ProblemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemJpaRepository problemRepository;

    @Transactional
    public Problem create(Problem problem) {
        ProblemJpa jpa = toJpa(problem);
        ProblemJpa saved = problemRepository.save(jpa);
        return toDomain(saved);
    }

    @Transactional(readOnly = true)
    public Problem findById(Long id) {
        return problemRepository.findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Problem> findAll(Pageable pageable) {
        return problemRepository.findAll(pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Problem> findByDifficulty(String difficulty, Pageable pageable) {
        return problemRepository.findByLevel(difficulty, pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Problem> searchByTitle(String title, Pageable pageable) {
        return problemRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Problem> findByClassId(Long classId, Pageable pageable) {
        return problemRepository.findByClassId(classId, pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Problem> findByClassIds(java.util.List<Long> classIds, Pageable pageable) {
        return problemRepository.findByClassIdIn(classIds, pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Problem> findByLevelAndClassIds(String difficulty, java.util.List<Long> classIds, Pageable pageable) {
        return problemRepository.findByLevelAndClassIdIn(difficulty, classIds, pageable)
                .map(this::toDomain);
    }

    @Transactional(readOnly = true)
    public Page<Problem> searchByTitleAndClassIds(String title, java.util.List<Long> classIds, Pageable pageable) {
        return problemRepository.findByTitleContainingIgnoreCaseAndClassIdIn(title, classIds, pageable)
                .map(this::toDomain);
    }


    @Transactional
    public Problem update(Long id, Problem problem) {
        ProblemJpa existing = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));

        existing.setTitle(problem.getTitle());
        existing.setContent(problem.getContent());
        existing.setLevel(problem.getLevel());
        existing.setTimeLimit(problem.getTimeLimit());
        existing.setMemoryLimit(problem.getMemoryLimit());

        ProblemJpa updated = problemRepository.save(existing);
        return toDomain(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!problemRepository.existsById(id)) {
            throw new RuntimeException("Problem not found with id: " + id);
        }
        problemRepository.deleteById(id);
    }

    private Problem toDomain(ProblemJpa jpa) {
        return Problem.builder()
                .id(jpa.getId())
                .problemCode(jpa.getProblemCode())
                .title(jpa.getTitle())
                .content(jpa.getContent())
                .level(jpa.getLevel())
                .timeLimit(jpa.getTimeLimit())
                .memoryLimit(jpa.getMemoryLimit())
                .createdBy(jpa.getCreatedBy())
                .classId(jpa.getClassId())
                .createdAt(jpa.getCreatedAt())
                .updatedAt(jpa.getUpdatedAt())
                .build();
    }

    private ProblemJpa toJpa(Problem domain) {
        return ProblemJpa.builder()
                .id(domain.getId())
                .problemCode(domain.getProblemCode())
                .title(domain.getTitle())
                .content(domain.getContent())
                .level(domain.getLevel())
                .timeLimit(domain.getTimeLimit())
                .memoryLimit(domain.getMemoryLimit())
                .createdBy(domain.getCreatedBy())
                .classId(domain.getClassId())
                .build();
    }
}
