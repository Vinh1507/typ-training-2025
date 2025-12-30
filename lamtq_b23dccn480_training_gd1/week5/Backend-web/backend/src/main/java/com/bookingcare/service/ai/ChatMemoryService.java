package com.bookingcare.service.ai;

import com.bookingcare.entity.ChatMemory;
import com.bookingcare.enums.Role;
import com.bookingcare.repository.MyMemoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMemoryService {
    private MyMemoryRepository myMemoryRepository;

    public ChatMemoryService(MyMemoryRepository myMemoryRepository) {
        this.myMemoryRepository = myMemoryRepository;
    }

    public void saveMessage(Long userId, Role role, String message) {
        ChatMemory chatMemory = new ChatMemory();
        chatMemory.setUserId(userId);
        chatMemory.setRole(role);
        chatMemory.setMessage(message);
        myMemoryRepository.save(chatMemory);
    }

    public List<ChatMemory> getUserHistory(Long userId) {
        return myMemoryRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }
}