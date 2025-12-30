package com.bookingcare.service.msgRabbitMQ;

import com.bookingcare.configuration.RabbitMQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
        System.out.println("Message sent to " + RabbitMQConfig.EXCHANGE_NAME + " : " + message);
    }
}
