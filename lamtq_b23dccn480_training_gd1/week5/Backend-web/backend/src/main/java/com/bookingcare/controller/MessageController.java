package com.bookingcare.controller;

import com.bookingcare.service.msgRabbitMQ.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageProducer producer;

    @GetMapping("/send")
    public String send(@RequestParam String msg) {
        producer.sendMessage(msg);
        return "Message sent: " + msg;
    }
}