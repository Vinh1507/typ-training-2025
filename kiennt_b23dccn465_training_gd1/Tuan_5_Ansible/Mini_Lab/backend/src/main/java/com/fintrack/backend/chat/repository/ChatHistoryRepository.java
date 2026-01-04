package com.fintrack.backend.chat.repository;

import com.fintrack.backend.chat.model.ChatHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    
    // Lấy lịch sử chat của user, sắp xếp theo thời gian mới nhất
    Page<ChatHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // Lấy tất cả lịch sử chat của user (không phân trang)
    List<ChatHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Xóa tất cả lịch sử chat của user
    void deleteByUserId(Long userId);
    
    // Đếm số lượng chat của user
    long countByUserId(Long userId);
}

