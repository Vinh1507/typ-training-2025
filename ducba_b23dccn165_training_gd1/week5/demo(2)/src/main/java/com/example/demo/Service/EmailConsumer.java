package com.example.demo.Service;

import com.example.demo.Config.RabbitMQConfig;
import com.example.demo.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receivebonjourEmailRequest(User user) {
        LOGGER.info(String.format("RabbitMQ Nhận yêu cầu gửi email chào mừng cho: %s", user.getEmail()));
        try {
            Thread.sleep(1000); // Delay 1000ms
            LOGGER.info(String.format("RabbitMQ Gửi email thành công cho %s", user.getEmail()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error(String.format("RabbitMQ Gửi email thất bại cho %s", user.getEmail()), e);
        }
    }
}
