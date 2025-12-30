package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_code", unique = true, nullable = false)
    private String roleCode; // ROLE_USER / ROLE_STAFF / ROLE_ADMIN

    @Column(name = "role_name")
    private String roleName;
}
