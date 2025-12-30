package com.bookingcare.controller;

import com.bookingcare.DTO.ChatRequestDTO;
import com.bookingcare.service.ai.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {
    ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(value = "/api/chat", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public String chat(@RequestBody ChatRequestDTO request){ // request giờ chứa Long userId
        return chatService.chat(request);
    }

    @PostMapping(value = "/api/chat-with-image", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
    public String chatWithImage(@RequestParam("file") MultipartFile file,
                                @RequestParam("message") String message,
                                @RequestParam("userId") Long userId){
        return chatService.chatWithImage(file, message, userId);
    }
}