package com.BTL_JAVA.BTL.DTO.Response.Chat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationItemResponse {
    int conversationId;
    String lastMessage;
    LocalDateTime updatedAt;
    SenderSummary senderSummary;
}
