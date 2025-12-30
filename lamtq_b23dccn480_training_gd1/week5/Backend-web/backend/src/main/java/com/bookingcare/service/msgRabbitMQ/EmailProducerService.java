package com.bookingcare.service.msgRabbitMQ;

import com.bookingcare.DTO.EmailMessageDTO;
import com.bookingcare.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Async
    public void sendEmailAsync(EmailMessageDTO emailMessageDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                emailMessageDTO
        );
    }
}