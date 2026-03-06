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
@Table(name = "roles")
@Builder
public class Role {

    @Id
    @Column(name = "name_roles")
    String nameRoles;

    @Column(name="descriptions")
    String description;

    @ManyToMany(fetch = FetchType.LAZY,cascade={CascadeType.MERGE,CascadeType.REFRESH,CascadeType.DETACH})
    @JoinTable(
            name = "roles_permission",
            joinColumns = @JoinColumn(name="name_roles"),
            inverseJoinColumns =@JoinColumn(name = "name_permission")
    )
    private Set<Permission> permissions;

    @ManyToMany(fetch = FetchType.LAZY,cascade ={CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "name_roles"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
