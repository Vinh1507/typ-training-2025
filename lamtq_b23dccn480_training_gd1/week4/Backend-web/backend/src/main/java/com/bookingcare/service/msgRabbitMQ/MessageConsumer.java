package com.bookingcare.service.msgRabbitMQ;

import com.bookingcare.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Message received from " + RabbitMQConfig.EXCHANGE_NAME + " : " + message);
    }
}
