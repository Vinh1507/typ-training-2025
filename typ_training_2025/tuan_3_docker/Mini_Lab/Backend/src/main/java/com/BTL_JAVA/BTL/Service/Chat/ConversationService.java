package com.BTL_JAVA.BTL.Service.Chat;

import com.BTL_JAVA.BTL.DTO.Response.Chat.ConversationItemResponse;
import com.BTL_JAVA.BTL.DTO.Response.Chat.SenderSummary;
import com.BTL_JAVA.BTL.Entity.Chat.Conversation;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Repository.Chat.ConversationRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;
    UserRepository userRepository;

    public static final int DEFAULT_ADMIN_ID = 1;

    @Transactional
    public Conversation addConversation(int userId) {
        return conversationRepository.findByUser_IdAndAdmin_Id(userId,DEFAULT_ADMIN_ID).
                orElseGet(() ->{
                    Conversation conversation = Conversation.builder()
                            .user(userRepository.findById(userId).orElseThrow())
                            .admin(userRepository.findById(DEFAULT_ADMIN_ID).orElseThrow())
                                    .build();
                    return conversationRepository.save(conversation);
                }
        );
    }

    SenderSummary toSenderSummary(User user) {
        return SenderSummary.builder()
                .senderId(user.getId())
                .avatar(user.getAvatar())
                .senderName(user.getFullName())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ConversationItemResponse> listConversationsForViewer(int viewerId) {
        boolean isAdminViewer = (viewerId == DEFAULT_ADMIN_ID);
        List<Conversation> conversations = isAdminViewer
                ? conversationRepository.findByAdmin_IdOrderByUpdatedAtDesc(viewerId)
                : conversationRepository.findByUser_IdOrderByUpdatedAtDesc(viewerId);

        return conversations.stream().map(c -> {
            User counterpart = isAdminViewer ? c.getUser() : c.getAdmin();
            return ConversationItemResponse.builder()
                    .conversationId(c.getConversationId())
                    .senderSummary(toSenderSummary(counterpart))
                    .lastMessage(c.getLastMessage())
                    .updatedAt(c.getUpdatedAt())
                    .build();
        }).toList();
    }


}
