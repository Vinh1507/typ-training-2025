package com.BTL_JAVA.BTL.Service.Chat;

import com.BTL_JAVA.BTL.DTO.Request.Chat.ChatMessageRequest;
import com.BTL_JAVA.BTL.DTO.Response.Chat.ChatMessageResponse;
import com.BTL_JAVA.BTL.DTO.Response.Chat.SenderSummary;
import com.BTL_JAVA.BTL.Entity.Chat.Conversation;
import com.BTL_JAVA.BTL.Entity.Chat.Message;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Repository.Chat.ChatMessageRepository;
import com.BTL_JAVA.BTL.Repository.Chat.ConversationRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageService {
    ConversationRepository conversationRepository;
    ChatMessageRepository chatMessageRepository;
    UserRepository userRepository;
    ConversationService conversationService;
    SocketIOServer socketIOServer;

    SenderSummary toSenderSummary(User user) {
        return SenderSummary.builder()
                .senderId(user.getId())
                .avatar(user.getAvatar())
                .senderName(user.getFullName())
                .build();
    }

    private ChatMessageResponse toMessageResponse(Message m, Integer currentUserId) {
        return ChatMessageResponse.builder()
                .messageId(m.getMessageId())
                .conversationId(m.getConversation().getConversationId())
                .senderId(m.getSender().getId(  ))
                .senderSummary(toSenderSummary(m.getSender()))
                .content(m.getContent())
                .me(Objects.equals(m.getSender().getId(), currentUserId))
                .createdAt(m.getCreatedAt().toString())
                .build();
    }

    @Transactional
    public ChatMessageResponse send(ChatMessageRequest req) {
        if (req.getContent() == null || req.getContent().isBlank()) {
            throw new IllegalArgumentException("Nội dung tin nhắn trống");
        }

        final int ADMIN_ID = ConversationService.DEFAULT_ADMIN_ID;
        User sender = userRepository.findById(req.getSenderId()).orElseThrow();

        final int userId;
        if (Objects.equals(sender.getId(), ADMIN_ID)) {
            // Admin gửi: bắt buộc targetUserId
            if (req.getReceiverId() == null) {
                throw new IllegalArgumentException("Admin gửi tin phải chỉ định targetUserId");
            }
            userId = req.getReceiverId();
        } else {
            // User gửi: luôn chat với admin cố định
            userId = sender.getId();
        }

        // Luôn đảm bảo có phòng user <-> admin
        Conversation conv = conversationService.addConversation(userId);

        // Lưu message
        Message m = new Message();
        m.setConversation(conv);
        m.setSender(sender);
        m.setContent(req.getContent());
        // createdAt do @CreationTimestamp lo
        chatMessageRepository.save(m);

        // Cập nhật conversation
        conv.setLastMessage(req.getContent());
        conv.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conv);

        //public socket event to client is conversation
        ChatMessageResponse response = toMessageResponse(m, sender.getId());
        socketIOServer.getRoomOperations("conversation_" + conv.getConversationId())
                .sendEvent("message", response);


        // “currentUserId” để set cờ me — ở đây mình coi người gửi là current
        return toMessageResponse(m, sender.getId());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> listByConversation(int conversationId, int viewerId) {
        var messages = chatMessageRepository
                .findAllByConversationConversationIdOrderByCreatedAtAsc(conversationId);
        return messages.stream()
                .map(m -> toMessageResponse(m, viewerId))
                .toList();
    }

}
