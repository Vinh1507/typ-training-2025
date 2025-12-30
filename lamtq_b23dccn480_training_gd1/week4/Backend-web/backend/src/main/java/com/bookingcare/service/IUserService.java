package com.bookingcare.service;

import com.bookingcare.DTO.*;
import com.bookingcare.entity.PatientEntity;
import com.bookingcare.entity.SpecializationEntity;
import com.bookingcare.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserEntity createUser(UserDTO userDTO) throws Exception;
    String login(String userName, String password) throws Exception;
    List<PatientResponseDTO> findAllPatients();
    Optional<PatientResponseDTO> findById(Long patientId);
    PatientResponseDTO updatePatient(Long patientId, PatientUpdateDTO updatedData);
    void deletePatient(Long patientId);
    UserEntity findByUserName(String userName) throws Exception;
    void setNewPassword(String email, String newPassword);
    PatientResponseDTO insertPatient(PatientUpdateDTO patientUpdateDTO);
    void restoreUser(Long id);
    List<PatientResponseDTO> findDeletedPatients();
    PatientProfileDTO getPatientInfor(String username);
    void updatePatientProfile(String username, PatientProfileDTO dto);
    void sendOtp(String email);
    boolean verifyOtp(String email, String otp);
}
