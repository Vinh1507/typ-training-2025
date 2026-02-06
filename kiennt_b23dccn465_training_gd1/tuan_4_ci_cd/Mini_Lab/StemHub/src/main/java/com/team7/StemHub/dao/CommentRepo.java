package com.team7.StemHub.dao;

import com.team7.StemHub.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepo extends JpaRepository<Comment, UUID> {
    @EntityGraph(attributePaths = { "user", "document" })
    List<Comment> findAllByDocumentIdOrderByCreatedAtDesc(UUID documentId);
}