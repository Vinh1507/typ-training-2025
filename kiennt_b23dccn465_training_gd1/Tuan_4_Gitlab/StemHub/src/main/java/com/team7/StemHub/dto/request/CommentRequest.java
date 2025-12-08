package com.team7.StemHub.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentRequest {
    @JsonProperty("documentId")
    private UUID documentId;
    @JsonProperty("userId")
    private UUID userId;
    @JsonProperty("content")
    private String content;
}
