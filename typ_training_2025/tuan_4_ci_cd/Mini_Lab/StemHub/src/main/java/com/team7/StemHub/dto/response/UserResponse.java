package com.team7.StemHub.dto.response;

import com.team7.StemHub.model.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UserResponse {
    private final UUID userId;
    private final String username;
    private final String fullname;
    private final String email;
    private final String role;
    private final Integer numberOfUploadedDocuments;
    private final LocalDateTime createdAt;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.fullname = user.getFullname();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
        this.numberOfUploadedDocuments = user.getUploadFiles() != null ? user.getUploadFiles().size() : 0;
        this.createdAt = user.getCreatedAt();
    }
}
