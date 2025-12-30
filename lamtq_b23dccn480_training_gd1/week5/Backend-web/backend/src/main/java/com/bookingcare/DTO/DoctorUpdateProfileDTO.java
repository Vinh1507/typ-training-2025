package com.bookingcare.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorUpdateProfileDTO {
    private String fullname;
    private String gender;
    private String address;
    private String phone;
    private Double price;
    private String newPassword;
}
