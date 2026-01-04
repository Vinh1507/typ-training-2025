package com.fintrack.backend.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // Đọc email "from" của SendGrid
    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public void sendPasswordResetEmail(String toEmail, String token) {
        String frontendUrl = "http://localhost:3001";
        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        String emailBody = "Bạn đã yêu cầu đặt lại mật khẩu.\n\n"
                + "Nhấp vào link sau để đặt lại:\n"
                + resetUrl + "\n\n"
                + "Link có hiệu lực trong 15 phút.";

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Yêu cầu đặt lại mật khẩu - FinTrack");
            message.setText(emailBody);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}