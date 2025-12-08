package com.team7.StemHub.dto.view;

import com.team7.StemHub.dto.response.DocumentResponse;
import com.team7.StemHub.dto.response.UserResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserProfileView {
    private UserResponse user;
    private Page<DocumentResponse> documents;
    private UUID currentUserId;
    private Set<UUID> likedIds;
}