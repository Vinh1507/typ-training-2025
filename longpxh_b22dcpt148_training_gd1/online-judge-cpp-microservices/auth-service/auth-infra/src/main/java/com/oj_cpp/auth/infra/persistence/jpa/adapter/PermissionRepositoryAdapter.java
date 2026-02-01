package com.oj_cpp.auth.infra.persistence.jpa.adapter;

import com.oj_cpp.auth.domain.model.Permission;
import com.oj_cpp.auth.domain.repository.PermissionRepository;
import com.oj_cpp.auth.infra.persistence.jpa.mapper.PermissionMapper;
import com.oj_cpp.auth.infra.persistence.jpa.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {
    
    private final PermissionJpaRepository jpaRepository;
    private final PermissionMapper mapper;

    @Override
    public Optional<Permission> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        return jpaRepository.findByRoleId(roleId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Permission save(Permission permission) {
        var jpa = mapper.toJpa(permission);
        var saved = jpaRepository.save(jpa);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Permission> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
