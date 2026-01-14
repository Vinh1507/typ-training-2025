package com.bookingcare.DTO;

import com.bookingcare.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientUpdateDTO {
//    private String name;
//    private LocalDate birth;
//    private Gender gender;
//    private String address;
//    private String phone;
//    private String email;
//    private String city;
//    private String district;
    private String name;
    private String email;
    private String password;
}