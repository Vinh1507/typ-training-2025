package com.oj.platform.components.iam.infrastructure.persistence.jpa.hydrator;

import com.oj.platform.components.iam.domain.model.permission.Permission;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.PermissionJpa;
import org.springframework.stereotype.Component;

@Component
public class PermissionJpaHydrator {
    public Permission hydrate(PermissionJpa jpa) {
        if (jpa == null) {
            return null;
        }
        return new Permission(
                jpa.getId(),
                jpa.getName(),
                jpa.getDescription(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }

    public PermissionJpa extract(Permission permission) {
        if (permission == null) {
            return null;
        }

        PermissionJpa jpa = new PermissionJpa();
        if (permission.getId() != null && !permission.getId().trim().isEmpty()) {
            jpa.setId(Long.parseLong(permission.getId()));
        }
        jpa.setName(permission.getName());
        jpa.setDescription(permission.getDescription());
        jpa.setCreatedAt(permission.getCreatedAt());
        jpa.setUpdatedAt(permission.getUpdatedAt());
        return jpa;
    }
}
