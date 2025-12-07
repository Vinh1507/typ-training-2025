package com.oj.platform.components.iam.infrastructure.persistence.jpa.hydrator;

import com.oj.platform.components.iam.domain.model.role.Role;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.RoleJpa;
import org.springframework.stereotype.Component;

@Component
public class RoleJpaHydrator {
    public Role hydrate(RoleJpa jpa) {
        if (jpa == null) {
            return null;
        }
        return new Role(
                jpa.getId(),
                jpa.getName(),
                jpa.getDescription(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }

    public RoleJpa extract(Role role) {
        if (role == null) {
            return null;
        }

        RoleJpa jpa = new RoleJpa();
        if (role.getId() != null && !role.getId().trim().isEmpty()) {
            jpa.setId(Long.parseLong(role.getId()));
        }
        jpa.setName(role.getName());
        jpa.setDescription(role.getDescription());
        jpa.setCreatedAt(role.getCreatedAt());
        jpa.setUpdatedAt(role.getUpdatedAt());
        return jpa;
    }
}
