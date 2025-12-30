package com.bookingcare.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileDTO {
    private Long id;
    private String fullname;
    private String birth;
    private String gender;
    private String address;
    private String phone;
    private String city;
    private String district;
    private String email;       // readonly, vẫn cần gửi để hiển thị
    private String newPassword;
}
