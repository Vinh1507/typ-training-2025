package com.bookingcare.service.msgRabbitMQ;

import com.bookingcare.DTO.EmailMessageDTO;
import com.bookingcare.configuration.RabbitMQConfig;
import com.bookingcare.service.IEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final IEmailService emailService;

    public EmailConsumer(IEmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(
            queues = RabbitMQConfig.QUEUE_NAME,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handleEmailMessage(EmailMessageDTO dto) {
        System.out.println("Received email to: " + dto.getTo());
        emailService.sendEmail(dto.getTo(), dto.getSubject(), dto.getContent());
    }
}

