package com.example.qr_backend.controller;

import com.example.qr_backend.entity.LoginTransaction;
import com.example.qr_backend.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    private final String BASE_URL = "https://typ-training-2025-backend-git-week4-lehuyitnt03-2901s-projects.vercel.app";

    @Autowired
    private RedisService redisService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateLogin(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        if(username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username is required"));
        }

        LoginTransaction loginTransaction = new LoginTransaction(username);
        System.out.println("This is the UUID \"tx\" for LoginTransaction: " + loginTransaction.getTx());
        redisService.saveTransaction(loginTransaction);

        String qrUrl = BASE_URL + "/confirm/" + loginTransaction.getTx();

        System.out.println(qrUrl);

        return ResponseEntity.ok(Map.of("qrUrl", qrUrl, "tx", loginTransaction));
    }

    @GetMapping("/verify/{txId}")
    public ResponseEntity<?> verifyTransaction(@PathVariable String txId) {
        // 1. Gọi Service để lấy thông tin (Thay vì dùng RedisTemplate trực tiếp)
        LoginTransaction tx = redisService.getTransaction(txId);

        // Case 1: Không tìm thấy hoặc hết hạn
        if (tx == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Mã QR đã hết hạn hoặc không tồn tại."));
        }

        // Case 2: Trả về thông tin (Bao gồm cả Status để Frontend tự xử lý logic chuyển trang)
        // Lưu ý: Tôi đã bỏ đoạn check !WAITING trả lỗi, vì Frontend cần biết khi nào là APPROVED
        return ResponseEntity.ok(Map.of(
                "username", tx.getUsername(),
                "status", tx.getStatus(),
                "txId", tx.getTx()
        ));
    }
}
