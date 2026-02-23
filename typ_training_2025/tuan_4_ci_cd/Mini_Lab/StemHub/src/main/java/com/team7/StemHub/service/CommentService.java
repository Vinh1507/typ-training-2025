package com.team7.StemHub.service;

import com.team7.StemHub.dao.CommentRepo;
import com.team7.StemHub.dao.DocumentRepo;
import com.team7.StemHub.dao.UserRepo;
import com.team7.StemHub.dto.request.CommentRequest;
import com.team7.StemHub.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final DocumentRepo documentRepo;

    public Comment addComment(CommentRequest commentRequest) {
        var user = userRepo.findById(commentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var document = documentRepo.findById(commentRequest.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found"));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setDocument(document);
        comment.setContent(commentRequest.getContent());
        return commentRepo.save(comment);
    }

    public List<Comment> getAllCommentByDocumentId(UUID documentId) {
        return commentRepo.findAllByDocumentIdOrderByCreatedAtDesc(documentId);
    }
}
