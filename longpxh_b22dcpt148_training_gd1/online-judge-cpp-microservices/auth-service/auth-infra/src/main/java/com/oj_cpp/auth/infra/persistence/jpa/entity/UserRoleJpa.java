package com.oj_cpp.auth.infra.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "user_roles")
@IdClass(UserRoleJpa.UserRoleId.class)
public class UserRoleJpa {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpa user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpa role;

    @Data
    public static class UserRoleId implements Serializable {
        private Long user;
        private Long role;
    }
}
