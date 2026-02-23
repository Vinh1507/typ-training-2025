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
public class ConversationResponse {
    int conversationId;
    int userId;
    int adminId;
    String lastMessage;
    LocalDateTime updatedAt;
    LocalDateTime createdAt;
}
