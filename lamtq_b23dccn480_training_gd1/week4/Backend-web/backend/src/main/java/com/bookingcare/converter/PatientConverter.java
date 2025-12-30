package com.bookingcare.converter;

import com.bookingcare.DTO.PatientResponseDTO;
import com.bookingcare.entity.PatientEntity;
import org.springframework.stereotype.Component;

@Component
public class PatientConverter {

    public PatientResponseDTO toResponseDTO(PatientEntity entity) {
        try {
            if (entity == null) {
                return null;
            }
            PatientResponseDTO responseDTO = new PatientResponseDTO();
            responseDTO.setId(entity.getId());
            responseDTO.setName(entity.getFullName());
            responseDTO.setBirth(entity.getBirth());
            responseDTO.setGender(entity.getGender());
            responseDTO.setAddress(entity.getAddress());
            responseDTO.setPhone(entity.getPhone());
            responseDTO.setEmail(entity.getEmail());
            responseDTO.setCity(entity.getCity());
            responseDTO.setDistrict(entity.getDistrict());
            return responseDTO;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
