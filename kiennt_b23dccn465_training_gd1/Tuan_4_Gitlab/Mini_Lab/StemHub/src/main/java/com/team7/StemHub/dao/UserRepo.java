package com.team7.StemHub.dao;

import com.team7.StemHub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Thêm import này
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = { "uploadFiles" })
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = { "uploadFiles" })
    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.uploadFiles ORDER BY size(u.uploadFiles) DESC LIMIT 10")
    List<User> findTop10OrderByDocumentNumberDesc();

    @Query("SELECT u FROM User u WHERE u.id = :id")
    @EntityGraph(attributePaths = { "favoritesDocuments" })
    Optional<User> findByIdWithFavorites(@Param("id") UUID id);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    @EntityGraph(attributePaths = { "uploadFiles" })
    Optional<User> findByIdWithUploads(@Param("id") UUID id);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    @EntityGraph(attributePaths = {
            "uploadFiles",
            "favoritesDocuments",
            "favoritesDocuments.author",
            "favoritesDocuments.course"
    })
    Optional<User> findByUsernameWithAllData(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            "ORDER BY u.fullname")
    @EntityGraph(attributePaths = { "uploadFiles" })
    Page<User> searchByFullnameOrUsername(@Param("keyword") String keyword, Pageable pageable);
}