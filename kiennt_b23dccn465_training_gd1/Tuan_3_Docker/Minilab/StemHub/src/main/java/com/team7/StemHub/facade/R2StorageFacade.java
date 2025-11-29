package com.team7.StemHub.facade;

import com.team7.StemHub.dto.request.DocumentRequest;
import com.team7.StemHub.model.Document;
import com.team7.StemHub.model.User;
import com.team7.StemHub.model.Course;
import com.team7.StemHub.service.CourseService;
import com.team7.StemHub.service.DocumentService;
import com.team7.StemHub.service.MediaService;
import com.team7.StemHub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.team7.StemHub.util.ExtensionUtil.getFileExtension;
import static com.team7.StemHub.util.ThumbnailUtil.createThumbnailFromPdf;

@Service
@RequiredArgsConstructor
@Slf4j
public class R2StorageFacade {

    private final MediaService mediaService;
    private final DocumentService documentService;
    private final UserService userService;
    private final CourseService courseService;

    @Transactional(rollbackFor = Exception.class)
    public Document uploadDocument(DocumentRequest dto) {
        try {
            // 1. Validate required data first - BEFORE uploading to R2
            log.info("Starting document upload validation for title: {}", dto.getTitle());

            // Validate user exists
            User author = null;
            if (dto.getAuthorId() != null) {
                try {
                    author = userService.getUserById(dto.getAuthorId());
                } catch (RuntimeException e) {
                    log.error("User not found with ID: {}", dto.getAuthorId());
                    throw new IllegalArgumentException("Author not found with ID: " + dto.getAuthorId(), e);
                }
            } else {
                throw new IllegalArgumentException("Author ID is required");
            }

            // Validate and get/create course
            Course course = null;
            if (dto.getCourseName() != null && !dto.getCourseName().trim().isEmpty()) {
                try {
                    course = courseService.getCourseByCourseName(dto.getCourseName().trim());
                } catch (Exception e) {
                    log.error("Error processing course: {}", dto.getCourseName(), e);
                    throw new IllegalArgumentException("Error processing course: " + dto.getCourseName(), e);
                }
            } else {
                throw new IllegalArgumentException("Course name is required");
            }

            // 2. Upload files to R2 ONLY after validation passes
            log.info("Validation passed, uploading files to R2");

            String fileUrl = null;
            String thumbnailUrl = null;

            try {
                // Check file extension and convert to PDF if needed
                String originalFilename = dto.getFile().getOriginalFilename();
                // Upload main file
                fileUrl = mediaService.uploadFile(dto.getFile());
                log.info("Main file uploaded successfully: {}", fileUrl);

                // Handle thumbnail
                if (dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()) {
                    thumbnailUrl = mediaService.uploadFile(dto.getThumbnail());
                    log.info("Thumbnail uploaded successfully: {}", thumbnailUrl);
                } else if (getFileExtension(fileUrl).equals("pdf")) {
                    try {
                        byte[] thumbnailBytes = createThumbnailFromPdf(dto.getFile());
                        thumbnailUrl = mediaService.uploadFile(thumbnailBytes, "thumbnail.jpg");
                        log.info("PDF thumbnail generated and uploaded: {}", thumbnailUrl);
                    } catch (Exception e) {
                        log.warn("Failed to generate PDF thumbnail, using empty string", e);
                        thumbnailUrl = "";
                    }
                }

            } catch (Exception e) {
                log.error("Failed to upload files to R2", e);
                throw new RuntimeException("Failed to upload files: " + e.getMessage(), e);
            }

            // 3. Create and save document to database
            log.info("Creating document entity");
            Document document = Document.builder()
                    .title(dto.getTitle())
                    .author(author)
                    .description(dto.getDescription())
                    .filePath(fileUrl)
                    .thumbnailPath(thumbnailUrl)
                    .category(dto.getCategory())
                    .course(course)
                    .createdAt(LocalDateTime.now())
                    .build();

            Document savedDocument = documentService.saveDocument(document);
            log.info("Document saved successfully with ID: {}", savedDocument.getId());

            return savedDocument;

        } catch (Exception e) {
            log.error("Error in uploadDocument: {}", e.getMessage(), e);
            // If we reach here, the transaction will be rolled back automatically
            // but files might already be uploaded to R2 (this is a known limitation)
            throw e;
        }
    }

}
