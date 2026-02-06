package com.team7.StemHub.facade;

import com.team7.StemHub.dto.request.DocumentRequest;
import com.team7.StemHub.dto.response.CommentResponse;
import com.team7.StemHub.dto.response.CourseResponse;
import com.team7.StemHub.dto.response.DocumentResponse;
import com.team7.StemHub.dto.view.DocumentDetailView;
import com.team7.StemHub.dto.view.UploadFormView;
import com.team7.StemHub.exception.FileUploadException;
import com.team7.StemHub.model.Comment;
import com.team7.StemHub.model.Document;
import com.team7.StemHub.model.User;
import com.team7.StemHub.model.enums.Category;
import com.team7.StemHub.service.CommentService;
import com.team7.StemHub.service.CourseService;
import com.team7.StemHub.service.DocumentService;
import com.team7.StemHub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentFacade {
    private final DocumentService documentService;
    private final R2StorageFacade r2StorageFacade;
    private final UserService userService;
    private final CommentService commentService;
    private final CourseService courseService;

    public UploadFormView prepareUploadFormData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = null;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userService.findByUsername(userDetails.getUsername());
            if (user != null) {
                userId = user.getId();
            }
        }

        List<CourseResponse> courses = courseService.getAllCourses().stream()
                .map(CourseResponse::new)
                .toList();

        return UploadFormView.builder()
                .categories(Category.values())
                .userId(userId)
                .courses(courses)
                .build();
    }

    public void handleDocumentUpload(DocumentRequest documentRequest) throws FileUploadException {
        if (documentRequest.getAuthorId() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && authentication.getPrincipal() instanceof UserDetails userDetails) {
                User user = userService.findByUsername(userDetails.getUsername());
                if (user != null) {
                    documentRequest.setAuthorId(user.getId());
                } else {
                    throw new FileUploadException ("Không thể tìm thấy người dùng. Vui lòng đăng nhập lại.", null);
                }
            } else {
                throw new FileUploadException("Yêu cầu xác thực. Vui lòng đăng nhập.", null);
            }
        }
        if (documentRequest.getFile() == null || documentRequest.getFile().isEmpty()) {
            throw new FileUploadException("Vui lòng chọn tệp tài liệu.", null);
        }
        String originalFilename = documentRequest.getFile().getOriginalFilename();
        String ext = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase()
                : "";
        String[] allowed = {"pdf","doc","docx","xls","xlsx","ppt","pptx","txt","md","jpg","jpeg","png","gif","bmp","webp"};
        boolean supported = java.util.Arrays.asList(allowed).contains(ext);

        if (!supported) {
            throw new FileUploadException("Định dạng không được hỗ trợ. Hãy chọn: PDF, DOC/DOCX, PPT/PPTX, XLS/XLSX, TXT, JPG/PNG/JPEG/WEBP/GIF/BMP.", null);
        }
        try {
            r2StorageFacade.uploadDocument(documentRequest);
        } catch (Exception e) {
            log.error("Lỗi từ R2StorageFacade: {}", e.getMessage(), e);
            throw new FileUploadException("Lỗi khi tải tệp lên: " + e.getMessage(), e);
        }
    }

    public DocumentDetailView getDocumentDetailView(UUID documentId) {
        Document document = documentService.getDocumentById(documentId);
        Long countFavorites = documentService.countFavorites(documentId);
        List<Document> relatedDocument = documentService.getRelatedDocument(document);
        List<Comment> lastComment = commentService.getAllCommentByDocumentId(documentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID currentUserId = null;
        boolean liked = false;
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User currentUser = userService.findByUsernameWithAllData(userDetails.getUsername());
            if (currentUser != null) {
                currentUserId = currentUser.getId();
                try {
                    liked = currentUser.getFavoritesDocuments() != null &&
                            currentUser.getFavoritesDocuments().stream()
                                    .anyMatch(d -> d != null && d.getId() != null && d.getId().equals(documentId));
                } catch (Exception ex) {
                    liked = false;
                }
            }
        }
        DocumentResponse documentDTO = new DocumentResponse(document, countFavorites);
        List<DocumentResponse> relatedDocumentDTO = relatedDocument.stream().map(DocumentResponse::new).toList();
        List<CommentResponse> lastCommentDTO = lastComment.stream().map(CommentResponse::new).toList();
        return DocumentDetailView.builder()
                .document(documentDTO)
                .relatedDocument(relatedDocumentDTO)
                .lastComment(lastCommentDTO)
                .currentUserId(currentUserId)
                .liked(liked)
                .build();
    }
}