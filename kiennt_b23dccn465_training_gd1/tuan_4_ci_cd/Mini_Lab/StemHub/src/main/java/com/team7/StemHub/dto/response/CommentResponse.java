package com.team7.StemHub.dto.response;

import com.team7.StemHub.model.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {
    private final String username;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.username = comment.getUser().getUsername();
        this.title = comment.getDocument().getTitle();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
