package com.bookingcare.DTO;

public record PaymentQRRequestDTO (
        Long appointmentId,
        Double amount,
        String description
) {
}