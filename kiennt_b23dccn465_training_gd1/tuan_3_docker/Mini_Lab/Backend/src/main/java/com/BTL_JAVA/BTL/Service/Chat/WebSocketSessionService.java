package com.BTL_JAVA.BTL.Service.Chat;

import com.BTL_JAVA.BTL.Entity.Chat.WebSocketSession;
import com.BTL_JAVA.BTL.Repository.Chat.WebSocketSessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketSessionService {
    WebSocketSessionRepository webSocketSessionRepository;

    public WebSocketSession createWebSocketSession(WebSocketSession webSocketSession) {
        return webSocketSessionRepository.save(webSocketSession);
    }

    public void deleteSession(String sessionId) {
        webSocketSessionRepository.deleteBySocketSessionId(sessionId);
    }
}
