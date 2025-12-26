package com.example.qr_backend.controller;

import com.example.qr_backend.service.TransactionServiceLegacy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/confirm")
@CrossOrigin(origins = "*")
public class ConfirmController {
    @Autowired
    private TransactionServiceLegacy transactionService;

    @PostMapping("/confirm")
    public ResponseEntity<?> handleConfirmation(@RequestBody Map<String, String> payload) {
        // 1. Receive the data sent from React
        String txId = payload.get("tx");      // Matches { tx: txId } in React
        System.out.println("This is the response from UI: " + txId);
        String action = payload.get("action"); // Matches { action: actionText } in React

        if (txId == null || action == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing tx or action"));
        }

        boolean isApproved = "confirm".equalsIgnoreCase(action);

        // 2. Process logic (Redis Atomic Check)
        int success = transactionService.processTransactionSafe(txId, isApproved);

        // 3. Return result
        // Xử lý kết quả trả về
        return switch (success) {
            case 1 -> // SUCCESS (Thành công)
                    ResponseEntity.ok(Map.of(
                            "status", "SUCCESS",
                            "message", isApproved ? "Login Authorized!" : "Login Rejected!"
                    ));
            case 0 -> // EXPIRED (Hết hạn - 404 Not Found)
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                            "status", "EXPIRED",
                            "message", "QR Code has expired or does not exist."
                    ));
            case -1 -> // CONFLICT (Đã xử lý rồi - 409 Conflict)
                    ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                            "status", "CONFLICT",
                            "message", "This request has already been processed by another device or thread."
                    ));
            default -> // Lỗi không xác định (500)
                    ResponseEntity.internalServerError().body(Map.of("message", "Unknown error occurred"));
        };
    }
}
