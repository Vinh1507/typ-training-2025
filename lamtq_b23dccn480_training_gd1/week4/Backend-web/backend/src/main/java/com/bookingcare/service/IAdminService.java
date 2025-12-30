package com.bookingcare.service;

import com.bookingcare.DTO.DoctorResponseDTO;
import com.bookingcare.DTO.DoctorUpdateDTO;
import com.bookingcare.DTO.SpecializationDTO;
import com.bookingcare.DTO.UserDTO;
import com.bookingcare.entity.SpecializationEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IAdminService {
    ResponseEntity<?> promotePatientToDoctor(Long id);
    List<DoctorResponseDTO> getDoctors();
    DoctorResponseDTO updateDoctor(Long id, DoctorUpdateDTO doctorUpdateDTO);
    void deleteDoctor(Long id);
    DoctorResponseDTO insertDoctor(DoctorUpdateDTO doctorUpdateDTO);
    List<DoctorResponseDTO> getDoctorDeleted();
    void restoreDoctor(Long id);
    List<String> findAllSpecializations();
    void addSpecialization(String specialization);
    void deleteSpecialization(String name);
}
