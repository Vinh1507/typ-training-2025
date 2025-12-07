package com.oj.platform.components.iam.infrastructure.persistence.jpa.implement;

import com.oj.platform.components.iam.domain.model.role.Role;
import com.oj.platform.components.iam.domain.model.role.RoleInterface;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.RoleJpa;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.hydrator.RoleJpaHydrator;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.repository.RoleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RoleJpaImpl implements RoleInterface {
    private final RoleJpaRepository jpaRepository;
    private final RoleJpaHydrator hydrator;

    public RoleJpaImpl(RoleJpaRepository jpaRepository, RoleJpaHydrator hydrator) {
        this.jpaRepository = jpaRepository;
        this.hydrator = hydrator;
    }

    @Override
    public Role createRole(Role role) {
        RoleJpa jpa = hydrator.extract(role);
        RoleJpa saved = jpaRepository.save(jpa);
        return hydrator.hydrate(saved);
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return jpaRepository.findById(id)
                .map(hydrator::hydrate);
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        return jpaRepository.findByName(name)
                .map(hydrator::hydrate);
    }

    @Override
    public List<Role> getAllRoles() {
        return jpaRepository.findAll().stream()
                .map(hydrator::hydrate)
                .collect(Collectors.toList());
    }

    @Override
    public Role updateRole(Role role) {
        RoleJpa jpa = hydrator.extract(role);
        RoleJpa saved = jpaRepository.save(jpa);
        return hydrator.hydrate(saved);
    }

    @Override
    public boolean deleteRole(Long id) {
        if (jpaRepository.existsById(id)) {
            jpaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
