package com.bookingcare.service.impl;

import com.bookingcare.DTO.AppointmentRequestDTO;
import com.bookingcare.DTO.AppointmentResponseDTO;
import com.bookingcare.DTO.BookingDTO;
import com.bookingcare.converter.AppointmentConvertToDTO;
import com.bookingcare.entity.*;
import com.bookingcare.enums.Status;
import com.bookingcare.repository.*;
import com.bookingcare.service.IAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements IAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AppointmentConvertToDTO appointmentConvertToDTO;

    @Transactional
    @Override
    public void createAppointment(BookingDTO dto, String currentUsername) {
        List<AppointmentEntity> appointmentEntityList = appointmentRepository.findAllByPatientEntity_EmailAndStatusNot(dto.getEmail(), Status.DA_HUY);
        for(AppointmentEntity appointmentEntity : appointmentEntityList) {
            if(appointmentEntity.getDate().toString().equals(dto.getDate())) {
                throw new RuntimeException("Không thể đặt 2 lịch khám cùng 1 bác sĩ trong cùng 1 ngày");
            }
        }
        Long timeHour = Long.parseLong(dto.getTime().substring(0,2));
        if(timeHour < 7 || (timeHour > 11 && timeHour < 14) || timeHour > 17) {
            throw new RuntimeException("Ngoài giờ làm việc vui lòng chọn giờ khác");
        }

        AppointmentEntity appointmentEntity = new  AppointmentEntity();
        LocalDate date = LocalDate.parse(dto.getDate());
        LocalTime time = LocalTime.parse(dto.getTime());
        appointmentEntity.setDate(date);
        appointmentEntity.setTime(time);
        appointmentEntity.setNote(dto.getNote());
        appointmentEntity.setReason(dto.getLyDoKham());

        appointmentEntity.setStatus(com.bookingcare.enums.Status.DANG_CHO);

        appointmentEntity.setBookingName(
                dto.getNguoiDatLichTen() != null && !dto.getNguoiDatLichTen().isEmpty()
                        ? dto.getNguoiDatLichTen()
                        : dto.getName()
        );
        appointmentEntity.setBookingPhone(
                dto.getNguoiDatLichSDT() != null && !dto.getNguoiDatLichSDT().isEmpty()
                        ? dto.getNguoiDatLichSDT()
                        : dto.getPhone()
        );
        appointmentEntity.setBookingEmail(
                dto.getEmailUser() != null && !dto.getEmailUser().isEmpty()
                        ? dto.getEmailUser()
                        : dto.getEmail()
        );
        Optional<UserEntity> optionalUser = userRepository.findByUserName(dto.getEmail());
        if(optionalUser.isPresent() && !optionalUser.get().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Email đã tồn tại vui lòng đăng nhập để sử dụng");
        }
        PatientEntity patientEntity = patientRepository.findByEmailAndDeletedFalse(dto.getEmail());
        // chua tung kham truoc day
        if (patientEntity == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(dto.getEmail());
            userEntity.setPassword(passwordEncoder.encode(dto.getPhone()));
            userEntity.setStatus(1L);
            RoleEntity defaultRole = roleRepository.findByRoleCode("ROLE_USER");
            userEntity.getRoles().add(defaultRole);
            userRepository.save(userEntity);

            patientEntity = new PatientEntity();
            patientEntity.setUser(userEntity);
            patientEntity.setFullName(dto.getName());
            patientEntity.setEmail(dto.getEmail());
            patientEntity.setBirth(dto.getNamSinh());
            patientEntity.setAddress(dto.getDiaChi());
            patientEntity.setGender(dto.getGender());
            patientEntity.setPhone(dto.getPhone());
            patientEntity.setCity(dto.getThanhPho());
            patientEntity.setDistrict(dto.getXaHuyen());
            patientEntity.setDeleted(false);
        }
        patientRepository.save(patientEntity);
        appointmentEntity.setPatientEntity(patientEntity);

        DoctorEntity doctorEntity = doctorRepository.findByIdAndIsDeletedFalse(dto.getIdBacSi())
                .orElseThrow(() -> new RuntimeException("Doctor không tồn tại"));
        appointmentEntity.setTotalCost(doctorEntity.getPrice());
        appointmentEntity.setDoctorEntity(doctorEntity);


        appointmentRepository.save(appointmentEntity);
    }

    @Override
    public List<AppointmentResponseDTO> getAllBookings() {
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAll();
        List<AppointmentResponseDTO> appointmentResponseDTOS = new ArrayList<>();
        for (AppointmentEntity appointmentEntity : appointmentEntities) {
            appointmentResponseDTOS.add(appointmentConvertToDTO.convertToDTO(appointmentEntity));
        }
        return appointmentResponseDTOS;
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO appointmentRequestDTO) {
        AppointmentEntity appointmentExistEntity = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointmentRequestDTO.getStatus() != null && !appointmentRequestDTO.getStatus().isEmpty()) {
            try {
                Status statusEnum = Status.fromLabel(appointmentRequestDTO.getStatus());
                appointmentExistEntity.setStatus(statusEnum);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Trạng thái không hợp lệ: " + appointmentRequestDTO.getStatus());
            }
        }
        if(appointmentRequestDTO.getNote() != null && !appointmentRequestDTO.getNote().isEmpty()) {
            appointmentExistEntity.setNote(appointmentRequestDTO.getNote());
        }
        appointmentRepository.save(appointmentExistEntity);
        return appointmentConvertToDTO.convertToDTO(appointmentExistEntity);
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentByUsername(String email) {
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAllByPatientEntity_Email(email);
        List<AppointmentResponseDTO> appointmentResponseDTOS = new ArrayList<>();
        for (AppointmentEntity appointmentEntity : appointmentEntities) {
            appointmentResponseDTOS.add(appointmentConvertToDTO.convertToDTO(appointmentEntity));
        }
        return appointmentResponseDTOS;
    }

    @Override
    public void deleteAppointment(Long id, String username) {
        AppointmentEntity appointmentEntity =  appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Lịch hẹn không tồn tại"));

        if(!appointmentEntity.getPatientEntity().getEmail().equals(username)){
            throw new RuntimeException("Bạn không có quyền hủy lịch này.");
        }

        if(Status.DA_HUY.equals(appointmentEntity.getStatus())){
            throw new RuntimeException("Lịch đã được huỷ trước đó rồi");
        }

        if(Status.DA_XAC_NHAN.equals(appointmentEntity.getStatus())){
            throw new RuntimeException("Lịch đã đã được xác nhận không thể huỷ");
        }

        appointmentEntity.setStatus(Status.DA_HUY);
        appointmentRepository.save(appointmentEntity);
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentByDoctorId(Long id) {
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAllByDoctorEntity_Id(id);
        List<AppointmentResponseDTO> appointmentResponseDTOS = new ArrayList<>();
        for (AppointmentEntity appointmentEntity : appointmentEntities) {
            appointmentResponseDTOS.add(appointmentConvertToDTO.convertToDTO(appointmentEntity));
        }
        return appointmentResponseDTOS;
    }

    @Override
    public void updateBookingStatus(Long id) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointmentEntity.setStatus(Status.DA_XAC_NHAN);
        appointmentRepository.save(appointmentEntity);
    }
}