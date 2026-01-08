package com.fintrack.backend.chat.controller;

import com.fintrack.backend.chat.model.ChatHistory;
import com.fintrack.backend.chat.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3002", "http://localhost:5173", "http://192.168.123.83:81"})
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;

    /**
     * Lưu lịch sử chat
     * POST /api/chat/history
     */
    @PostMapping("/history")
    public ResponseEntity<ChatHistory> saveChatHistory(
            @RequestBody ChatRequest request
    ) {
        // Tạm thời dùng userId từ request, sau này lấy từ JWT token
        Long userId = request.getUserId() != null ? request.getUserId() : 1L;
        
        ChatHistory saved = chatHistoryService.saveChatHistory(
                userId,
                request.getMessage(),
                request.getResponse()
        );
        
        return ResponseEntity.ok(saved);
    }

    /**
     * Lấy lịch sử chat với phân trang
     * GET /api/chat/history?userId=1&page=0&size=20
     */
    @GetMapping("/history")
    public ResponseEntity<Page<ChatHistory>> getChatHistory(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<ChatHistory> history = chatHistoryService.getChatHistory(userId, page, size);
        return ResponseEntity.ok(history);
    }

    /**
     * Lấy tất cả lịch sử chat (không phân trang)
     * GET /api/chat/history/all?userId=1
     */
    @GetMapping("/history/all")
    public ResponseEntity<List<ChatHistory>> getAllChatHistory(
            @RequestParam(defaultValue = "1") Long userId
    ) {
        List<ChatHistory> history = chatHistoryService.getAllChatHistory(userId);
        return ResponseEntity.ok(history);
    }

    /**
     * Xóa một chat history
     * DELETE /api/chat/history/{id}
     */
    @DeleteMapping("/history/{id}")
    public ResponseEntity<Map<String, String>> deleteChatHistory(@PathVariable Long id) {
        chatHistoryService.deleteChatHistory(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa lịch sử chat thành công"));
    }

    /**
     * Xóa tất cả lịch sử chat của user
     * DELETE /api/chat/history/all?userId=1
     */
    @DeleteMapping("/history/all")
    public ResponseEntity<Map<String, String>> deleteAllChatHistory(
            @RequestParam(defaultValue = "1") Long userId
    ) {
        chatHistoryService.deleteAllChatHistory(userId);
        return ResponseEntity.ok(Map.of("message", "Đã xóa tất cả lịch sử chat thành công"));
    }

    /**
     * Đếm số lượng chat của user
     * GET /api/chat/history/count?userId=1
     */
    @GetMapping("/history/count")
    public ResponseEntity<Map<String, Long>> countChatHistory(
            @RequestParam(defaultValue = "1") Long userId
    ) {
        long count = chatHistoryService.countChatHistory(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * DTO cho request lưu chat history
     */
    public static class ChatRequest {
        private Long userId;
        private String message;
        private String response;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}

