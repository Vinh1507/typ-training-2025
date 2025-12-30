package com.bookingcare.converter;

import com.bookingcare.DTO.AppointmentResponseDTO;
import com.bookingcare.entity.AppointmentEntity;
import org.springframework.stereotype.Component;

@Component
public class AppointmentConvertToDTO {
    public AppointmentResponseDTO convertToDTO(AppointmentEntity entity) {
        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setTime(entity.getTime());
        dto.setStatus(entity.getStatus().getLabel());
        dto.setNote(entity.getNote());
        dto.setReason(entity.getReason());
        dto.setTotalCost(entity.getTotalCost());
        dto.setCreatedAt(entity.getCreatedAt());

        dto.setBookingName(entity.getBookingName());
        dto.setBookingEmail(entity.getBookingEmail());
        dto.setBookingPhone(entity.getBookingPhone());

        dto.setName(entity.getPatientEntity().getFullName());
        dto.setGioitinh(entity.getPatientEntity() != null && entity.getPatientEntity().getGender() != null
                ? entity.getPatientEntity().getGender().name()
                : "");
        dto.setNamsinh(entity.getPatientEntity().getBirth());
        dto.setDiachi(entity.getPatientEntity().getAddress());
        dto.setThanhpho(entity.getPatientEntity().getCity());
        dto.setEmail(entity.getPatientEntity().getEmail());
        dto.setPhone(entity.getPatientEntity().getPhone());

        dto.setBacsi(entity.getDoctorEntity().getFullName());
        dto.setDepartment(entity.getDoctorEntity().getClinic().getName());
        dto.setDiachikham(entity.getDoctorEntity().getClinic().getDescription());
        return dto;
    }

}