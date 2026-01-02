package com.example.week4.Controller;

import com.example.week4.dto.OrderCreatedEvent;
import com.example.week4.model.Order;
import com.example.week4.repository.OrderRepository;
import com.example.week4.service.OrderEventProducer;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    public OrderController(OrderRepository orderRepository, OrderEventProducer eventProducer) {
        this.orderRepository = orderRepository;
        this.eventProducer = eventProducer;
    }

    @PostMapping
    public String createOrder(@RequestParam String email,
                              @RequestParam BigDecimal total) {

        // 1. Lưu vào DB
        Order order = new Order(email, total);
        order = orderRepository.save(order);

        // 2. Tạo event
        OrderCreatedEvent event = new OrderCreatedEvent(
                "order-created-" + UUID.randomUUID(),
                order.getId(),
                order.getEmail(),
                order.getTotal(),
                Instant.now()
        );

        // 3. Publish Kafka
        eventProducer.publishOrderCreated(event);

        // 4. Trả về cho client
        return "Order created with id = " + order.getId();
    }
}
