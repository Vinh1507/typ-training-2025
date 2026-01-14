package com.bookingcare.service.impl;

import com.bookingcare.DTO.DoctorDTO;
import com.bookingcare.DTO.DoctorProfileDTO;
import com.bookingcare.DTO.DoctorResponseDTO;
import com.bookingcare.DTO.DoctorUpdateProfileDTO;
import com.bookingcare.converter.ConvertDoctorEntityToDetail;
import com.bookingcare.converter.ConvertFromDoctorEntityToDTO;
import com.bookingcare.entity.DoctorEntity;
import com.bookingcare.entity.UserEntity;
import com.bookingcare.enums.Gender;
import com.bookingcare.repository.DoctorRepository;
import com.bookingcare.repository.UserRepository;
import com.bookingcare.service.IDoctorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements IDoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ConvertDoctorEntityToDetail convertDoctorEntityToDetail;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<DoctorDTO> findDoctorBySpecialization(String specialization) {
        List<DoctorEntity> doctorEntityList = doctorRepository.findAllBySpecial_NameAndIsDeletedFalse(specialization);
        List<DoctorDTO> doctorDTOList = new ArrayList<>();
        for (DoctorEntity doctorEntity : doctorEntityList) {
            DoctorDTO doctorDTO = convertDoctorEntityToDetail.convert(doctorEntity);
            doctorDTOList.add(doctorDTO);
        }
        return doctorDTOList;
    }

    @Override
    public DoctorProfileDTO getDoctorInfor(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUserName(username);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("Không tìm thấy thông tin bác sĩ");
        }
        else{
            UserEntity userEntity = optionalUser.get();
            DoctorProfileDTO doctorProfileDTO = new DoctorProfileDTO();
            doctorProfileDTO.setId(userEntity.getId());
            doctorProfileDTO.setSpecialization(userEntity.getDoctorEntity().getSpecial().getName());
            doctorProfileDTO.setFullname(userEntity.getDoctorEntity().getFullName());
            doctorProfileDTO.setGender(
                    userEntity.getDoctorEntity().getGender() != null
                            ? userEntity.getDoctorEntity().getGender().name()
                            : null
            );
            doctorProfileDTO.setAddress(userEntity.getDoctorEntity().getAddress());
            doctorProfileDTO.setClinicAddress(userEntity.getDoctorEntity().getClinic().getDescription());
            doctorProfileDTO.setClinicName(userEntity.getDoctorEntity().getClinic().getName());
            doctorProfileDTO.setPhone(userEntity.getDoctorEntity().getPhone());
            doctorProfileDTO.setEmail(userEntity.getDoctorEntity().getEmail());
            doctorProfileDTO.setPrice(userEntity.getDoctorEntity().getPrice());
            return doctorProfileDTO;
        }
    }

    @Override
    public void updateDoctorProfile(DoctorUpdateProfileDTO doctorUpdateProfileDTO, String userName) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException("User không tồn tại"));
        if (doctorUpdateProfileDTO.getNewPassword() != null && !doctorUpdateProfileDTO.getNewPassword().isEmpty()) {
            String hashed = passwordEncoder.encode(doctorUpdateProfileDTO.getNewPassword());
            user.setPassword(hashed);
            userRepository.save(user);
        }

        DoctorEntity doctor = doctorRepository.findByEmailAndIsDeletedFalse(userName)
                .orElseThrow(() -> new EntityNotFoundException("Doctor không tồn tại"));
        doctor.setFullName(doctorUpdateProfileDTO.getFullname());
        doctor.setGender(Gender.valueOf(doctorUpdateProfileDTO.getGender()));
        doctor.setAddress(doctorUpdateProfileDTO.getAddress());
        doctor.setPhone(doctorUpdateProfileDTO.getPhone());
        doctor.setPrice(doctorUpdateProfileDTO.getPrice());
        doctorRepository.save(doctor);
    }

}
