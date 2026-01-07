package com.example.qlsv_database.controller;

import com.example.qlsv_database.mq.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mq")
@RequiredArgsConstructor
public class MQController {

    private final MessageProducer messageProducer;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody String message) {
        try {
            messageProducer.send(message);
            return ResponseEntity.ok("Message sent to RabbitMQ: " + message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send message: " + e.getMessage());
        }
    }
}
