package com.bookingcare.repository;

import com.bookingcare.entity.ChatMemory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MyMemoryRepository extends JpaRepository<ChatMemory, Long> {

    List<ChatMemory> findByUserIdOrderByCreatedAtAsc(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMemory c WHERE c.createdAt < :cutoff")
    int deleteOlderThan(@Param("cutoff") LocalDateTime cutoff);
}