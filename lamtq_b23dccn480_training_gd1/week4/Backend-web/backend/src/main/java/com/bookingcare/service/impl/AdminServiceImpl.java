package com.bookingcare.service.impl;

import com.bookingcare.DTO.DoctorResponseDTO;
import com.bookingcare.DTO.DoctorUpdateDTO;
import com.bookingcare.DTO.SpecializationDTO;
import com.bookingcare.converter.ConvertFromDoctorEntityToDTO;
import com.bookingcare.entity.*;
import com.bookingcare.repository.*;
import com.bookingcare.service.IAdminService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements IAdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private ConvertFromDoctorEntityToDTO convertFromDoctorEntityToDTO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Transactional
    @Override
    public ResponseEntity<?> promotePatientToDoctor(Long patientId) {
        PatientEntity patient = patientRepository.findByIdAndDeletedFalse(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient không tồn tại"));
        UserEntity user = patient.getUser();
        patient.setDeleted(true);
        patientRepository.save(patient);
        ClinicEntity defaultClinic = clinicRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Clinic mặc định không tồn tại"));

        DoctorEntity doctor = new DoctorEntity();
        doctor.setUser(user);
        doctor.setClinic(defaultClinic);
        doctor.setSpecial(null);
        doctor.setIsDeleted(false);
        doctor.setFullName(patient.getFullName());
        doctor.setEmail(patient.getEmail());
        doctor.setPhone(patient.getPhone());
        doctor.setAddress(patient.getAddress());
        doctor.setGender(patient.getGender());
        doctorRepository.save(doctor);

        RoleEntity doctorRole = roleRepository.findByRoleCode("ROLE_DOCTOR");
        if (doctorRole == null) {
            throw new EntityNotFoundException("Role DOCTOR không tồn tại");
        }
        user.getRoles().clear();
        user.getRoles().add(doctorRole);
        userRepository.save(user);

        return ResponseEntity.ok("Đã chuyển Patient sang Doctor thành công");
    }

    @Override
    public List<DoctorResponseDTO> getDoctors() {
        List<DoctorEntity> doctors = doctorRepository.findAllByIsDeletedFalse();
        List<DoctorResponseDTO> dtos = new ArrayList<>();
        for (DoctorEntity doctor : doctors) {
            dtos.add(convertFromDoctorEntityToDTO.toDTO(doctor));
        }
        return dtos;
    }

    @Transactional
    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorUpdateDTO doctorUpdateDTO) {
        DoctorEntity existingDoctor = doctorRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // email dang dang nhap
        if (doctorUpdateDTO.getEmail().equals(currentUsername)) {
            throw new RuntimeException("Không thể dùng email trùng với tài khoản đang đăng nhập!");
        }
        if (!existingDoctor.getEmail().equals(doctorUpdateDTO.getEmail())
                && userRepository.existsByUserName(doctorUpdateDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        String fullName = doctorUpdateDTO.getName();
        String email = doctorUpdateDTO.getEmail();
        String specializationName = doctorUpdateDTO.getSpecialization();
        String newPassword = doctorUpdateDTO.getPassword();

        UserEntity userEntity = existingDoctor.getUser();
        userEntity.setUserName(email);
        // neu khong dien password
        if (newPassword == null || newPassword.isBlank()) {
            userEntity.setPassword(existingDoctor.getUser().getPassword());
        } else {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(userEntity);

        existingDoctor.setUser(userEntity);
        existingDoctor.setFullName(fullName);
        existingDoctor.setEmail(email);
        if (specializationName != null && !specializationName.isBlank()) {
            SpecializationEntity specialization = specializationRepository
                    .findByNameIgnoreCase(specializationName)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));
            existingDoctor.setSpecial(specialization);
            existingDoctor.setSpeciality(specializationName);
        }
        doctorRepository.save(existingDoctor);

        DoctorResponseDTO doctorResponseDTO = convertFromDoctorEntityToDTO.toDTO(existingDoctor);
        doctorResponseDTO.setId(existingDoctor.getId());
        return doctorResponseDTO;
    }

    @Transactional
    @Override
    public void deleteDoctor(Long id) {
        DoctorEntity doctorEntity = doctorRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        doctorEntity.setIsDeleted(true);
        doctorRepository.save(doctorEntity);
    }

    @Transactional
    @Override
    public DoctorResponseDTO insertDoctor(DoctorUpdateDTO doctorUpdateDTO) {
        if(userRepository.existsByUserName(doctorUpdateDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại, không thể thêm mới!");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (doctorUpdateDTO.getEmail().equals(currentUsername)) {
            throw new RuntimeException("Không thể dùng email trùng với tài khoản đang đăng nhập!");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(doctorUpdateDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(doctorUpdateDTO.getPassword()));
        userEntity.setStatus(1L);
        RoleEntity defaultRole = roleRepository.findByRoleCode("ROLE_DOCTOR");
        userEntity.getRoles().add(defaultRole);
        userRepository.save(userEntity);

        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setFullName(doctorUpdateDTO.getName());
        doctorEntity.setEmail(doctorUpdateDTO.getEmail());
        String specializationName = doctorUpdateDTO.getSpecialization();
        if (specializationName != null && !specializationName.isBlank()) {
            SpecializationEntity specialization = specializationRepository
                    .findByNameIgnoreCase(specializationName)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));
            doctorEntity.setSpecial(specialization);
        }
        doctorEntity.setUser(userEntity);
        ClinicEntity defaultClinic = clinicRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Clinic mặc định không tồn tại"));
        doctorEntity.setClinic(defaultClinic);
        doctorEntity.setIsDeleted(false);
        doctorRepository.save(doctorEntity);

        return convertFromDoctorEntityToDTO.toDTO(doctorEntity);
    }

    @Override
    public List<DoctorResponseDTO> getDoctorDeleted() {
        return doctorRepository.findAllByIsDeletedTrue().stream()
                .map(convertFromDoctorEntityToDTO :: toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void restoreDoctor(Long id) {
        DoctorEntity doctorEntity = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        doctorEntity.setIsDeleted(false);
        doctorRepository.save(doctorEntity);
    }

    @Override
    public List<String> findAllSpecializations() {
        List<String> names = specializationRepository.findAll()
                .stream()
                .map(SpecializationEntity::getName)
                .distinct()
                .sorted()
                .toList();
        return names;
    }

    @Transactional
    @Override
    public void addSpecialization(String specialization) {
        SpecializationEntity specializationEntity = new SpecializationEntity();
        specializationEntity.setName(specialization);
        specializationRepository.save(specializationEntity);
    }

    @Override
    public void deleteSpecialization(String name) {
        Optional<SpecializationEntity> specializationOpt = specializationRepository.findByName(name);
        if (specializationOpt.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy chuyên khoa");
        }

        SpecializationEntity specializationEntity = specializationOpt.get();
        Long count = doctorRepository.countBySpecial(specializationEntity);
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Không thể xóa chuyên khoa '" + specializationEntity.getName() + "' vì vẫn còn bác sĩ thuộc chuyên khoa này.");
        }

        specializationRepository.delete(specializationEntity);
    }

}
