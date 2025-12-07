package com.bookingcare.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileDTO {
    private Long id;
    private String fullname;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private Double price;
    private String clinicName;
    private String clinicAddress;
    private String specialization;
}
