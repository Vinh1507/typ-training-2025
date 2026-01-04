package com.BTL_JAVA.BTL.Repository.Chat;

import com.BTL_JAVA.BTL.Entity.Chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatMessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByConversationConversationIdOrderByCreatedAtAsc(int conversationId);
}
