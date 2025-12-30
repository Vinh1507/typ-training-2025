package com.bookingcare.DTO;

import lombok.Builder;

@Builder
public record PaymentQRResponseDTO (
        Long appointmentId,
        String transactionId,
        Double amount,
        String qrCode, // Base64 QR code
        String paymentUrl, // URL để scan QR
        String description,
        Long expiresIn
) {
}