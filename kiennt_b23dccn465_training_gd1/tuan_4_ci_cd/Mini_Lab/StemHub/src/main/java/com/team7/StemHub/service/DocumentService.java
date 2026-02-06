package com.team7.StemHub.service;

import com.team7.StemHub.dao.CourseRepo;
import com.team7.StemHub.dao.DocumentRepo;
import com.team7.StemHub.exception.NotFoundException;
import com.team7.StemHub.model.Course;
import com.team7.StemHub.model.Document;
import com.team7.StemHub.model.User;
import com.team7.StemHub.model.enums.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepo;
    private final CourseService courseService;
    private final CourseRepo courseRepo;

    public Long countFavorites(UUID documentId) {
        return documentRepo.countFavoritesById(documentId);
    }

    public Document saveDocument(Document document) {
        return documentRepo.save(document);
    }

    public List<Document> getTopDocument() {
        return documentRepo.findTop5ByOrderByDownloadCountDesc();
    }

    public Page<Document> getNewestDocuments(int page) {
        Pageable pageable = PageRequest.of(page, 7);
        return documentRepo.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Document getDocumentById(UUID id) {
        return documentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public List<Document> getRelatedDocument(Document currentDocument) {
        return documentRepo.findTop10ByCourseAndCategoryAndIdNotOrderByDownloadCountDesc(
                currentDocument.getCourse(),
                currentDocument.getCategory(),
                currentDocument.getId()
        );
    }

    public List<Document> getDocumentsByCourse(Course course) {
        return documentRepo.findByCourse(course);
    }

    public void downloadDocument(UUID documentId) {
        Document document = getDocumentById(documentId);
        document.setDownloadCount(document.getDownloadCount() + 1);
        documentRepo.save(document);
    }

    public Page<Document> getAllUploadDocumentsByAuthor(User user, int page) {
        return documentRepo.findAllByAuthorOrderByCreatedAtDesc(user, PageRequest.of(page, 6));
    }

    public List<Document> getDocumentsByCategorySortedByDownloadCount(String category) {
        return documentRepo.findTop15ByCategoryOrderByDownloadCountDesc(Category.valueOf(category));
    }

    public Page<Document> getDocumentsByCategorySortedByCreatedAt(String category, int page){
        Pageable pageable = PageRequest.of(page, 7);
        return documentRepo.findByCategoryOrderByCreatedAtDesc(Category.valueOf(category), pageable);
    }

    public Page<Document> searchDocuments(String keyword, int page) {
        Pageable pageRequest = PageRequest.of(page, 6);
        return documentRepo.searchByKeyword(keyword, pageRequest);
    }
}
