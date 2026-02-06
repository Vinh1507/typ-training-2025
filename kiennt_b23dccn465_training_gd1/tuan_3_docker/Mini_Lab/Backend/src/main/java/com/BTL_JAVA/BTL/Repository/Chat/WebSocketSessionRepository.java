package com.BTL_JAVA.BTL.Repository.Chat;

import com.BTL_JAVA.BTL.Entity.Chat.WebSocketSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketSessionRepository extends JpaRepository<WebSocketSession, String> {
    void deleteBySocketSessionId(String sessionId);
}
