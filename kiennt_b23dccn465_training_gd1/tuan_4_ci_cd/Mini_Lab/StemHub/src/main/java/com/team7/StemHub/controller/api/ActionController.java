package com.team7.StemHub.controller.api;

import com.team7.StemHub.dto.request.CommentRequest;
import com.team7.StemHub.service.CommentService;
import com.team7.StemHub.service.DocumentService;
import com.team7.StemHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/actions")
public class ActionController {
    private final CommentService commentService;
    private final UserService userService;
    private final DocumentService documentService;

    @PostMapping(value = "/comment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postComment(@RequestBody CommentRequest commentRequest) {
        commentService.addComment(commentRequest);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping(value = "/like", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> likeDocument(@RequestParam UUID userId, @RequestParam UUID documentId) {
        boolean favorited = userService.likeDocument(userId, documentId);
        return ResponseEntity.ok(Map.of("success", true, "favorited", favorited));
    }

    @PostMapping(value = "/download", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> downloadDocument(@RequestParam UUID documentId) {
        documentService.downloadDocument(documentId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
