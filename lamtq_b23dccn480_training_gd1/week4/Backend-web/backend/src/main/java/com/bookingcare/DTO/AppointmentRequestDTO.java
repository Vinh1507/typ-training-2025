package com.bookingcare.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequestDTO {
    private Long id;
    private String status;
    private String note;
}