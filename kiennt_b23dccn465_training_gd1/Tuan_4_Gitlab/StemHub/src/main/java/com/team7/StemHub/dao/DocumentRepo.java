package com.team7.StemHub.dao;

import com.team7.StemHub.model.Course;
import com.team7.StemHub.model.Document;
import com.team7.StemHub.model.User;
import com.team7.StemHub.model.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // <-- Đảm bảo bạn import
import java.util.UUID;

@Repository
public interface DocumentRepo extends JpaRepository<Document, UUID> {

    @Override
    @EntityGraph(attributePaths = { "author", "course" })
    Optional<Document> findById(UUID id);

    @EntityGraph(attributePaths = { "author", "course" })
    Page<Document> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = { "author", "course" })
    List<Document> findTop5ByOrderByDownloadCountDesc();

    @Query("SELECT COUNT(DISTINCT u.id) FROM User u JOIN u.favoritesDocuments d WHERE d.id = :documentId")
    Long countFavoritesById(@Param("documentId") UUID documentId);

    @EntityGraph(attributePaths = { "author", "course" })
    List<Document> findByCourse(Course course);

    @EntityGraph(attributePaths = { "author", "course" })
    Page<Document> findAllByAuthorOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query("SELECT d FROM Document d JOIN d.course c WHERE " +
            "LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY d.title")
    @EntityGraph(attributePaths = { "author", "course" })
    Page<Document> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = { "author", "course" })
    List<Document> findTop15ByCategoryOrderByDownloadCountDesc(Category category);

    @EntityGraph(attributePaths = { "author", "course" })
    Page<Document> findByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);

    @EntityGraph(attributePaths = { "author", "course" })
    List<Document> findTop10ByCourseAndCategoryAndIdNotOrderByDownloadCountDesc(Course course, Category category, UUID id);
}