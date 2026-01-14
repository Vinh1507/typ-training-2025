package com.bookingcare.converter;

import com.bookingcare.DTO.DoctorResponseDTO;
import com.bookingcare.entity.DoctorEntity;
import org.springframework.stereotype.Component;

@Component
public class ConvertFromDoctorEntityToDTO {

    public DoctorResponseDTO  toDTO(DoctorEntity doctor) {
        DoctorResponseDTO dto = new DoctorResponseDTO();
        dto.setId(doctor.getId());
        dto.setEmail(doctor.getEmail());
        dto.setName(doctor.getFullName());
        dto.setSpecialization(
                doctor.getSpecial() != null
                        ? doctor.getSpecial().getName()
                        : "Chưa có chuyên khoa"
        );

        return dto;
    }
}
