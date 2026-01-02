package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {
    @Id
    private Long id;

    private String name;
    private String email;

    @Column(name = "updated_at")
    private Instant updatedAt;

}
