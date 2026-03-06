package com.BTL_JAVA.BTL.DTO.Response.Chat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SenderSummary {
    int senderId;
    String senderName;
    String avatar;
}
