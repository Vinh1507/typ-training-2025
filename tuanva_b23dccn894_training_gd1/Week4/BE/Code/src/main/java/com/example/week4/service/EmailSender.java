package com.example.week4.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class EmailSender {
    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

    public void sendOrderConfirmation(String email, Long orderId, BigDecimal total) {
        // Ở đây tạm thời chỉ log, sau này bạn tích hợp SMTP thật cũng được
        log.info("Sending confirmation email to {} for order {} with total {}", email, orderId, total);
    }
}
