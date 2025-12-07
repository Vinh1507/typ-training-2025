package com.bookingcare.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorUpdateDTO {
   private String name;
   private String email;
   private String specialization;
   private String password;
}
