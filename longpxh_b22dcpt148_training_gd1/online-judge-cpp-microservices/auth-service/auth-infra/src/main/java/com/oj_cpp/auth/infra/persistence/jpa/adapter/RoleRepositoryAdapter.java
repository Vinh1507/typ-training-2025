package com.oj_cpp.auth.infra.persistence.jpa.adapter;

import com.oj_cpp.auth.domain.model.Role;
import com.oj_cpp.auth.domain.repository.RoleRepository;
import com.oj_cpp.auth.infra.persistence.jpa.mapper.RoleMapper;
import com.oj_cpp.auth.infra.persistence.jpa.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {
    
    private final RoleJpaRepository jpaRepository;
    private final RoleMapper mapper;

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Role> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Role save(Role role) {
        var jpa = mapper.toJpa(role);
        var saved = jpaRepository.save(jpa);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
