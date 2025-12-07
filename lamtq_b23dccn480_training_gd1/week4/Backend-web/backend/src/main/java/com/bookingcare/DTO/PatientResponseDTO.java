package com.bookingcare.DTO;

import com.bookingcare.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private String name;
    private String birth;
    private Gender gender;
    private String address;
    private String phone;
    private String email;
    private String city;
    private String district;
}