package com.BTL_JAVA.BTL.DTO.Request.Chat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageRequest {
    Integer senderId;
    Integer receiverId;
    String content;
}
