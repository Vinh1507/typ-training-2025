package com.bookingcare.controller;

import com.bookingcare.DTO.PaymentConfirmRequestDTO;
import com.bookingcare.DTO.PaymentQRRequestDTO;
import com.bookingcare.DTO.PaymentQRResponseDTO;
import com.bookingcare.enums.Status;
import com.bookingcare.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {
    private final PaymentService paymentService;

    //tạo qr thanh toán
    @PostMapping("/generate-qr")
    public ResponseEntity<PaymentQRResponseDTO> generatePaymentQR(@RequestBody PaymentQRRequestDTO request) {
        PaymentQRResponseDTO response = paymentService.generatePaymentQR(request);
        return ResponseEntity.ok(response);
    }

    //xác nhận thanh toán
    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirmPayment(@RequestBody PaymentConfirmRequestDTO request) {
        System.out.println("========================================");
        System.out.println("✅ CONFIRM PAYMENT ENDPOINT REACHED!");
        System.out.println("Transaction ID: " + request.transactionId());
        System.out.println("Success: " + request.isSuccess());
        System.out.println("========================================");

        boolean success = paymentService.confirmPayment(request);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Thanh toán thành công" : "Thanh toán thất bại");

        return ResponseEntity.ok(response);
    }

    //kiểm tra trạng thái thanh toán
    @GetMapping("/status/{appointmentId}")
    public ResponseEntity<Map<String, Object>> checkPaymentStatus(@PathVariable Long appointmentId) {
        Status status = paymentService.getPaymentStatus(appointmentId);
        boolean isPaid = paymentService.isPaid(appointmentId);

        Map<String, Object> response = new HashMap<>();
        response.put("appointmentId", appointmentId);
        response.put("status", status);
        response.put("isPaid", isPaid);
        return ResponseEntity.ok(response);
    }
}