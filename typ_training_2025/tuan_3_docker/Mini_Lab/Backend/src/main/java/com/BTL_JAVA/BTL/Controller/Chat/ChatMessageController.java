package com.BTL_JAVA.BTL.Controller.Chat;

import com.BTL_JAVA.BTL.DTO.Request.Chat.ChatMessageRequest;
import com.BTL_JAVA.BTL.DTO.Response.Chat.ChatMessageResponse;
import com.BTL_JAVA.BTL.DTO.Response.Chat.ConversationResponse;
import com.BTL_JAVA.BTL.Service.Chat.ChatMessageService;
import com.BTL_JAVA.BTL.Service.Chat.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ChatMessageController {
    ConversationService conversationService;
    ChatMessageService messageService;

    // (Tuỳ chọn) đảm bảo có phòng ngay khi cần
    @PostMapping("/conversations/ensure")
    public ConversationResponse ensureConversation(@RequestParam Integer userId) {
        var c = conversationService.addConversation(userId);
        return ConversationResponse.builder()
                .conversationId(c.getConversationId())
                .userId(c.getUser().getId())
                .adminId(c.getAdmin().getId())
                .lastMessage(c.getLastMessage())
                .updatedAt(c.getUpdatedAt())
                .build();
    }

    // Gửi tin: user → admin mặc định, hoặc admin → user (bằng targetUserId)
    @PostMapping("/messages")
    public ChatMessageResponse sendMessage(@RequestBody ChatMessageRequest req) {
        return messageService.send(req);
    }

    @GetMapping("/messages")
    public List<ChatMessageResponse> listMessages(
            @RequestParam int conversationId,
            @RequestParam int viewerId // prod: lấy từ JWT
    ) {
        return messageService.listByConversation(conversationId, viewerId);
    }
}
