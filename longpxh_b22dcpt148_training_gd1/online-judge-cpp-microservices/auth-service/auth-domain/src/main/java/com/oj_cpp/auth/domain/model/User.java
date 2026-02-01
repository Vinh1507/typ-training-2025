package com.oj_cpp.auth.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class User {
    private final Long id;
    private final String username;
    private final String email;
    private final String fullName;
    private final String passwordHash;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;

    public User(Long id, String username, String email, String fullName, 
                String passwordHash, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getIdAsString() {
        return id != null ? id.toString() : null;
    }

    public boolean isValid() {
        return username != null && !username.isEmpty() 
            && email != null && !email.isEmpty()
            && passwordHash != null && !passwordHash.isEmpty();
    }
}
