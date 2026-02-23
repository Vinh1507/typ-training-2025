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
public class ChatMessageResponse {
    int messageId;
    int conversationId;
    int senderId;
    String content;
    boolean me;
    String createdAt;
    SenderSummary senderSummary;

}
