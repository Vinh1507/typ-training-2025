package com.example.week4.service;

import com.example.week4.dto.OrderCreatedEvent;
import com.example.week4.store.ProcessedEventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEmailConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderEmailConsumer.class);

    private final ProcessedEventStore processedEventStore;
    private final EmailSender emailSender;

    public OrderEmailConsumer(ProcessedEventStore processedEventStore, EmailSender emailSender) {
        this.processedEventStore = processedEventStore;
        this.emailSender = emailSender;
    }

    @KafkaListener(topics = "order.created", groupId = "order-email-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received event: orderId={}, email={}", event.getOrderId(), event.getEmail());

        // Idempotency: tránh xử lý trùng
        if (processedEventStore.isProcessed(event.getEventId())) {
            log.info("Event {} already processed, skip", event.getEventId());
            return;
        }

        // Logic gửi email
        emailSender.sendOrderConfirmation(event.getEmail(), event.getOrderId(), event.getTotal());

        processedEventStore.markProcessed(event.getEventId());
    }
}
