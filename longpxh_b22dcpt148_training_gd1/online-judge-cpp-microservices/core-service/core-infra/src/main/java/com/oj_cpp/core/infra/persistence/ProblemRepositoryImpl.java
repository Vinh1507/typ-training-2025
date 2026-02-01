package com.oj_cpp.core.infra.persistence;

import com.oj_cpp.core.domain.model.Problem;
import com.oj_cpp.core.domain.repository.ProblemRepository;
import com.oj_cpp.core.infra.persistence.jpa.entity.ProblemJpa;
import com.oj_cpp.core.infra.persistence.jpa.mapper.ProblemMapper;
import com.oj_cpp.core.infra.persistence.jpa.repository.ProblemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {
    private final ProblemJpaRepository problemJpaRepository;
    private final ProblemMapper problemMapper;

    @Override
    public Problem save(Problem problem) {
        ProblemJpa entity = problemMapper.toEntity(problem);
        ProblemJpa saved = problemJpaRepository.save(entity);
        return problemMapper.toDomain(saved);
    }

    @Override
    public Optional<Problem> findById(Long id) {
        return problemJpaRepository.findById(id)
                .map(problemMapper::toDomain);
    }

    @Override
    public Optional<Problem> findByProblemCode(String problemCode) {
        return problemJpaRepository.findByProblemCode(problemCode)
                .map(problemMapper::toDomain);
    }



    @Override
    public void deleteById(Long id) {
        problemJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByProblemCode(String problemCode) {
        return problemJpaRepository.existsByProblemCode(problemCode);
    }

    @Override
    public Optional<Problem> findTopByOrderByProblemCodeDesc() {
        return problemJpaRepository.findTopByOrderByProblemCodeDesc()
                .map(problemMapper::toDomain);
    }

    @Override
    public List<Problem> search(String keyword, String code, String title, String level, Long classId) {
        org.springframework.data.jpa.domain.Specification<ProblemJpa> spec = org.springframework.data.jpa.domain.Specification.where(null);

        if (code != null && !code.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("problemCode"), "%" + code + "%"));
        }

        if (title != null && !title.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("title"), "%" + title + "%"));
        }

        if (keyword != null && !keyword.isEmpty()) {
            String likePattern = "%" + keyword + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(root.get("title"), likePattern),
                            cb.like(root.get("problemCode"), likePattern)
                    )
            );
        }

        if (level != null && !level.isEmpty()) {
            try {
                com.oj_cpp.core.domain.enums.ProblemLevelEnum levelEnum = com.oj_cpp.core.domain.enums.ProblemLevelEnum.valueOf(level.toUpperCase());
                spec = spec.and((root, query, cb) -> cb.equal(root.get("level"), levelEnum));
            } catch (IllegalArgumentException e) {
            }
        }

        if (classId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("classId"), classId));
        }

        return problemJpaRepository.findAll(spec).stream()
                .map(problemMapper::toDomain)
                .collect(Collectors.toList());
    }
}
