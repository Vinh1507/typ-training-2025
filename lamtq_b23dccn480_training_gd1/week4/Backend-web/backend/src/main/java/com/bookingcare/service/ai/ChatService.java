package com.bookingcare.service.ai;

import com.bookingcare.DTO.ChatRequestDTO;
import com.bookingcare.entity.ChatMemory;
import com.bookingcare.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {
    private final ChatClient chatClient;
    private final ChatMemoryService chatMemoryService;
    private final DoctorAIService  doctorAIService;
    private final ClinicAIService clinicAIService;
    private final SpecializationAIService specializationAIService;

    private static final int MAX_HISTORY_MESSAGES = 20;
    private static final int MAX_RETRIES = 5;
    private static final long INITIAL_DELAY_MS = 1000; // 1 giây

    public ChatService(ChatClient.Builder builder,
                       ChatMemoryService chatMemoryService,
                       DoctorAIService doctorAIService, ClinicAIService clinicAIService, SpecializationAIService specializationAIService) {
        chatClient = builder.build();
        this.chatMemoryService = chatMemoryService;
        this.doctorAIService = doctorAIService;
        this.clinicAIService = clinicAIService;
        this.specializationAIService = specializationAIService;
    }

    private String getPromptSystem() {
        String doctorInfor = doctorAIService.getAllDoctorInfor();
        String clinicInfor = clinicAIService.getAllClinicInfor();
        String specializationInfor = specializationAIService.getAllSpecializationInfor();
        return """
                CRITICAL RULE: You are Doctor Java who is a assistant of my clinic
                CRITICAL RULE: You should reply to messages briefly and to the point politely
                CRITICAL RULE: You only answer medical questions or questions related to login, registration, system functionality
                CRITICAL RULE: You MUST respond in the SAME language as the user's last message.
                (Ví dụ: Nếu người dùng hỏi 'Xin chào', bạn PHẢI trả lời bằng tiếng Việt.)
                (Example: If the user asks 'Hello', you MUST respond in English.)
                
                DOCTOR_INFORMATION:
                This is a list of all doctors in the system.
                Use this information to answer users' questions about doctors.
                If a user asks for doctor information, only use information from this list.
                Do not make up information that is not in the list.
                %s
                
                CLINIC_INFORMATION:
                This is a list of all clinics in the system.
                Use this information to answer users' questions about clinics.
                If a user asks for clinic information, only use information from this list.
                Do not make up information that is not in the list.
                %s
                
                SPECIALIZATION_INFORMATION:
                This is the list of specialties
                Use this information to answer users' questions..
                If a user asks for specialization information, only use information from this list.
                Add your own descriptive details to provide explanations to the users.
                %s
                """.formatted(doctorInfor, clinicInfor, specializationInfor);
    }

    private List<Message> getRecentHistory(Long userId) {
        List<ChatMemory> fullHistory = chatMemoryService.getUserHistory(userId);

        int totalMessages = fullHistory.size();
        List<ChatMemory> recentHistory = fullHistory.subList(
                Math.max(0, totalMessages - MAX_HISTORY_MESSAGES),
                totalMessages
        );

        return recentHistory.stream()
                .map(chat -> chat.getRole() == Role.USER
                        ? new UserMessage(chat.getMessage())
                        : new AssistantMessage(chat.getMessage()))
                .collect(Collectors.toList());
    }

    private String callAiWithRetry(List<Message> history, String userMessageText, Media media) {
        long delay = INITIAL_DELAY_MS;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return chatClient.prompt()
                        .system(getPromptSystem())
                        .messages(history)
                        .user(promptUserSpec -> {
                            if (media != null) promptUserSpec.media(media);
                            promptUserSpec.text(userMessageText);
                        })
                        .call()
                        .content();
            } catch (org.springframework.ai.retry.NonTransientAiException e) {
                if (e.getMessage().contains("429")) {
                    log.warn("API rate limit 429, retry lần {} sau {} ms", attempt, delay);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.warn("Thread sleep bị gián đoạn", ie);
                    }
                    delay *= 2; // tăng thời gian chờ (exponential backoff)
                } else {
                    throw e; // lỗi khác thì ném luôn
                }
            }
        }
        throw new RuntimeException("Đã vượt quá số lần retry tối đa do lỗi 429");
    }

    public String chat(ChatRequestDTO request) {
        Long userId = request.userId();
        String userMessageText = request.message();

        List<Message> history = getRecentHistory(userId);

        String response = callAiWithRetry(history, userMessageText, null);

        chatMemoryService.saveMessage(userId, Role.USER, userMessageText);
        chatMemoryService.saveMessage(userId, Role.ASSISTANT, response);

        return response;
    }

    public String chatWithImage(MultipartFile file, String message, Long userId) {
        List<Message> history = getRecentHistory(userId);

        Media media;
        try {
            media = new Media(MimeTypeUtils.parseMimeType(file.getContentType()), file.getResource());
        } catch (Exception e) {
            log.error("Lỗi khi xử lý file media: {}", e.getMessage());
            return "Đã xảy ra lỗi khi đọc file ảnh.";
        }

        String response = callAiWithRetry(history, message, media);

        chatMemoryService.saveMessage(userId, Role.USER, message);
        chatMemoryService.saveMessage(userId, Role.ASSISTANT, response);

        return response;
    }
}