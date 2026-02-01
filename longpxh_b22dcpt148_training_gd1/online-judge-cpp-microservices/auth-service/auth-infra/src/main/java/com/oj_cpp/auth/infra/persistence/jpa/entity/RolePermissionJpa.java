package com.oj_cpp.auth.infra.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "role_permissions")
@IdClass(RolePermissionJpa.RolePermissionId.class)
public class RolePermissionJpa {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpa role;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private PermissionJpa permission;

    @Data
    public static class RolePermissionId implements Serializable {
        private Long role;
        private Long permission;
    }
}
