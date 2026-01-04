package com.fintrack.backend.chat.service;

import com.fintrack.backend.chat.model.ChatHistory;
import com.fintrack.backend.chat.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    /**
     * Lưu lịch sử chat vào database
     */
    @Transactional
    public ChatHistory saveChatHistory(Long userId, String message, String response) {
        ChatHistory chatHistory = ChatHistory.builder()
                .userId(userId)
                .message(message)
                .response(response)
                .build();
        
        return chatHistoryRepository.save(chatHistory);
    }

    /**
     * Lấy lịch sử chat của user với phân trang
     */
    public Page<ChatHistory> getChatHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    /**
     * Lấy tất cả lịch sử chat của user (không phân trang)
     */
    public List<ChatHistory> getAllChatHistory(Long userId) {
        return chatHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Xóa một chat history
     */
    @Transactional
    public void deleteChatHistory(Long id) {
        chatHistoryRepository.deleteById(id);
    }

    /**
     * Xóa tất cả lịch sử chat của user
     */
    @Transactional
    public void deleteAllChatHistory(Long userId) {
        chatHistoryRepository.deleteByUserId(userId);
    }

    /**
     * Đếm số lượng chat của user
     */
    public long countChatHistory(Long userId) {
        return chatHistoryRepository.countByUserId(userId);
    }
}

