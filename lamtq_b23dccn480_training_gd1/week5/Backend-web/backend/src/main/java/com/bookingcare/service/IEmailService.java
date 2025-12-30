package com.bookingcare.service;

import org.springframework.stereotype.Service;

public interface IEmailService {
    void sendEmail(String to, String subject, String content);

}
