package com.BTL_JAVA.BTL.Controller.Chat;

import com.BTL_JAVA.BTL.DTO.Response.Chat.ConversationItemResponse;
import com.BTL_JAVA.BTL.Service.Chat.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ConversationController {
    ConversationService conversationService;
    @GetMapping()
    public List<ConversationItemResponse> listConversations(@RequestParam int viewerId) {
        return conversationService.listConversationsForViewer(viewerId);
    }
}
