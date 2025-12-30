package com.bookingcare.service;

import com.bookingcare.DTO.DoctorDTO;
import com.bookingcare.DTO.DoctorProfileDTO;
import com.bookingcare.DTO.DoctorResponseDTO;
import com.bookingcare.DTO.DoctorUpdateProfileDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IDoctorService {
    List<DoctorDTO> findDoctorBySpecialization(String specialization);
    DoctorProfileDTO getDoctorInfor(String username);
    void updateDoctorProfile(DoctorUpdateProfileDTO doctorUpdateProfileDTO, String userName);
}
