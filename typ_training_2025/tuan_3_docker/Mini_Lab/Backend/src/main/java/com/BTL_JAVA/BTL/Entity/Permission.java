package com.BTL_JAVA.BTL.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "permission")
@Builder
public class Permission {

    @Id
    @Column(name = "name_permission")
    String namePermission;

    @Column(name = "descriptions")
    String description;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.DETACH})
    @JoinTable(
            name = "roles_permission",
            joinColumns = @JoinColumn(name = "name_permission"),
            inverseJoinColumns = @JoinColumn(name ="name_roles")
    )
    Set<Role> roles;
}
