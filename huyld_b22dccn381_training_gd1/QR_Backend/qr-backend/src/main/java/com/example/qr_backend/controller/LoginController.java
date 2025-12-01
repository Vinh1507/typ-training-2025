package com.example.qr_backend.controller;

import com.example.qr_backend.entity.LoginTransaction;
import com.example.qr_backend.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    private final String BASE_URL = "http://localhost:5173/";

    @Autowired
    private RedisService redisService;

//    @Autowired
//    private SimpMessagingTemplate messagingTemplate; // Để gửi WebSocket

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateLogin(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        if(username.isEmpty() || username == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username is required"));
        }

        LoginTransaction loginTransaction = new LoginTransaction(username);
        redisService.saveTransaction(loginTransaction);

        String qrUrl = BASE_URL + "/confirm/" + loginTransaction.getTx();

        System.out.println(qrUrl);

        return ResponseEntity.ok(Map.of("qrUrl", qrUrl, "tx", loginTransaction));
    }
}
