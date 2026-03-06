package com.team7.StemHub.dto.view;

import com.team7.StemHub.dto.response.CommentResponse;
import com.team7.StemHub.dto.response.DocumentResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DocumentDetailView {
    private DocumentResponse document;
    private List<DocumentResponse> relatedDocument;
    private List<CommentResponse> lastComment;
    private UUID currentUserId;
    private boolean liked;
}