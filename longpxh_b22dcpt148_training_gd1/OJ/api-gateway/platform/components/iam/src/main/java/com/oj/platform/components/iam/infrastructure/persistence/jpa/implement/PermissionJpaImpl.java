package com.oj.platform.components.iam.infrastructure.persistence.jpa.implement;

import com.oj.platform.components.iam.domain.model.permission.Permission;
import com.oj.platform.components.iam.domain.model.permission.PermissionInterface;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.PermissionJpa;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.hydrator.PermissionJpaHydrator;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.repository.PermissionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PermissionJpaImpl implements PermissionInterface {
    private final PermissionJpaRepository jpaRepository;
    private final PermissionJpaHydrator hydrator;

    public PermissionJpaImpl(PermissionJpaRepository jpaRepository, PermissionJpaHydrator hydrator) {
        this.jpaRepository = jpaRepository;
        this.hydrator = hydrator;
    }

    @Override
    public Permission createPermission(Permission permission) {
        PermissionJpa jpa = hydrator.extract(permission);
        PermissionJpa saved = jpaRepository.save(jpa);
        return hydrator.hydrate(saved);
    }

    @Override
    public Optional<Permission> getPermissionById(Long id) {
        return jpaRepository.findById(id)
                .map(hydrator::hydrate);
    }

    @Override
    public Optional<Permission> getPermissionByName(String name) {
        return jpaRepository.findByName(name)
                .map(hydrator::hydrate);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return jpaRepository.findAll().stream()
                .map(hydrator::hydrate)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return jpaRepository.findByRoleId(roleId).stream()
                .map(hydrator::hydrate)
                .collect(Collectors.toList());
    }

    @Override
    public Permission updatePermission(Permission permission) {
        PermissionJpa jpa = hydrator.extract(permission);
        PermissionJpa saved = jpaRepository.save(jpa);
        return hydrator.hydrate(saved);
    }

    @Override
    public boolean deletePermission(Long id) {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
