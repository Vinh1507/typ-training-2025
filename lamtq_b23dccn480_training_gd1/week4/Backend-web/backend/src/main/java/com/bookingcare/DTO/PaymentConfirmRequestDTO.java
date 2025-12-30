package com.bookingcare.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentConfirmRequestDTO(
        String transactionId,

        @JsonProperty("success")
        boolean isSuccess
) {
}