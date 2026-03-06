package com.BTL_JAVA.BTL.Entity.Chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "web_socket_session")
@Builder
public class WebSocketSession {
    @Id
    String id;

    String socketSessionId;

    String userId;

    Instant createdAt;

}
