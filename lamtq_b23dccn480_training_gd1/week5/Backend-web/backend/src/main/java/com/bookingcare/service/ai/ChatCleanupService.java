package com.bookingcare.service.ai;

import com.bookingcare.repository.MyMemoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatCleanupService {

    private final MyMemoryRepository chatMemoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatCleanupService.class);
    private final long minutes = 2;

    public ChatCleanupService(MyMemoryRepository chatMemoryRepository) {
        this.chatMemoryRepository = chatMemoryRepository;
    }

    // xóa dữ liệu mỗi 2 phút (đơn vị ms)
    @Scheduled(fixedRate = minutes * 60 * 1000)
    @Transactional
    public void cleanupOldChatMessages() {
        // lấy thời điểm 2 phút trước
        LocalDateTime cutOffDate = LocalDateTime.now().minusMinutes(minutes);

        logger.info("Đang chạy tác vụ dọn dẹp. Sẽ xóa tin nhắn trước: {}", cutOffDate);

        try {
            long deletedCount = chatMemoryRepository.deleteOlderThan(cutOffDate);
            if (deletedCount > 0) {
                logger.info("Đã xóa thành công {} tin nhắn cũ hơn {} phút.", deletedCount, minutes);
            } else {
                logger.info("Không có tin nhắn nào cũ hơn {} phút để xóa.", minutes);
            }
        } catch (Exception e) {
            logger.error("Lỗi khi xóa tin nhắn cũ: ", e);
        }
    }
}