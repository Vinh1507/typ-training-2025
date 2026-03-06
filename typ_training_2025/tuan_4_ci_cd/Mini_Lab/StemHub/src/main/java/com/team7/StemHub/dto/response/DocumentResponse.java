package com.team7.StemHub.dto.response;

import com.team7.StemHub.model.Document;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class DocumentResponse {
    private final UUID documentId;
    private final String title;
    private final String description;
    private final String authorName;
    private final String category;
    private final String courseName;
    private final String fileUrl;
    private final String thumbnailUrl;
    private final LocalDateTime createdAt;
    private final Integer downloadCount;
    private final Long favoriteCount;

    public DocumentResponse(Document document, Long favoriteCount) {
        this.documentId = document != null ? document.getId() : null;
        this.title = document != null ? document.getTitle() : null;
        this.description = document != null ? document.getDescription() : null;
        if (document != null && document.getAuthor() != null) {
            this.authorName = document.getAuthor().getFullname() != null ? document.getAuthor().getFullname() : document.getAuthor().getUsername();
        } else {
            this.authorName = "Anonymous";
        }
        this.category = document != null && document.getCategory() != null ? document.getCategory().toString() : null;
        this.courseName = (document != null && document.getCourse() != null) ? document.getCourse().getCourseName() : null;
        this.fileUrl = document != null ? document.getFilePath() : null;
        this.thumbnailUrl = document != null ? document.getThumbnailPath() : null;
        this.createdAt = document != null ? document.getCreatedAt() : null;
        this.downloadCount = document != null ? document.getDownloadCount() : null;
        this.favoriteCount = favoriteCount;
    }

    public DocumentResponse(Document document) {
        this(document, null);
    }
}
