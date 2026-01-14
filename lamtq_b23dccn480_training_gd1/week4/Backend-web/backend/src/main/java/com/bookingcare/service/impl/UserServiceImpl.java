package com.bookingcare.service.impl;

import com.bookingcare.DTO.PatientProfileDTO;
import com.bookingcare.DTO.PatientResponseDTO;
import com.bookingcare.DTO.PatientUpdateDTO;
import com.bookingcare.DTO.UserDTO;
import com.bookingcare.converter.PatientConverter;
import com.bookingcare.entity.*;
import com.bookingcare.enums.Gender;
import com.bookingcare.filters.JwtTokenFilter;
import com.bookingcare.repository.*;
import com.bookingcare.service.IEmailService;
import com.bookingcare.service.IUserService;
import com.bookingcare.utils.JwtTokenUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PatientConverter patientConverter;
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PasswordResetOtpRepository passwordResetOtpRepository;
    @Autowired
    private IEmailService  emailService;

    @Transactional
    @Override
    public UserEntity createUser(UserDTO userDTO) throws Exception {
        String email = userDTO.getEmail();
        if(userRepository.existsByUserName(email)){
            throw new DataIntegrityViolationException("Email Already Exists");
        }

        //convert userDTO => userEntity
        UserEntity newUserEntity = UserEntity.builder()
                .userName(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(new ArrayList<>())
                .status(1L)
                .build();
        RoleEntity defaultRole = roleRepository.findByRoleCode("ROLE_USER");
        newUserEntity.getRoles().add(defaultRole);
        userRepository.save(newUserEntity);

        // Luu vao patient entity
        PatientEntity patientEntity = PatientEntity.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .user(newUserEntity)
                .deleted(false)
                .build();
        patientRepository.save(patientEntity);
        return newUserEntity;
    }

    @Override
    public String login(String userName, String password) throws Exception {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new Exception("Invalid username or password"));
        boolean isAdmin = userEntity.getRoles().stream()
                .anyMatch(role -> role.getRoleCode().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            boolean isActivePatient = userRepository.findByUserNameAndPatientEntity_DeletedFalse(userName).isPresent();

            boolean isActiveDoctor = userRepository.findByUserNameAndDoctorEntity_IsDeletedFalse(userName).isPresent();

            if (!isActivePatient && !isActiveDoctor) {
                throw new Exception("Tài khoản này đã bị vô hiệu hoá hoặc không tồn tại!");
            }
        }
        // check password
        if(!passwordEncoder.matches(password,userEntity.getPassword())){
            throw new BadCredentialsException("Wrong phone number or password");
        }

        // load user ==> authentication token luu name, password real va authorities
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password, userEntity.getAuthorities());
        // tim bean authenticationManager de gan UsernamePasswordAuthenticationToken de xac thuc
        authenticationManager.authenticate(authenticationToken);
        // login thanh cong => sinh token
        return jwtTokenUtil.generateToken(userEntity); // token duoc sinh ra se duoc su dung de vao cac api, truoc khi vao cac api dung token vao websecurityConfig de xem quyen
    }

    @Override
    public void sendOtp(String email) {
        UserEntity user = userRepository.findByUserName(email)
                .orElseThrow(() -> new NoSuchElementException("Email không tồn tại"));

        String otp = String.valueOf(new Random().nextInt(999999));

        PasswordResetOTP otpEntity = new PasswordResetOTP();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(3));

        passwordResetOtpRepository.save(otpEntity);

        emailService.sendEmail(email, "Mã OTP đặt lại mật khẩu", "OTP của bạn là: " + otp);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        PasswordResetOTP entity = passwordResetOtpRepository.findTopByEmailOrderByIdDesc(email);
        if (entity == null) return false;
        if (!entity.getOtp().equals(otp)) return false;
        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) return false;

        return true;
    }


    @Override
    public void setNewPassword(String email, String newPassword){
        UserEntity user = userRepository.findByUserName(email)
                .orElseThrow(() -> new NoSuchElementException("Email không tồn tại trong hệ thống."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public List<PatientResponseDTO> findAllPatients() {
        return patientRepository.findAllByDeletedFalse().stream()
                .map(patientConverter::toResponseDTO) // Chuyển Entity sang DTO
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponseDTO> findDeletedPatients() {
        return patientRepository.findAllByDeletedTrue().stream()
                .map(patientConverter::toResponseDTO) // Chuyển Entity sang DTO
                .collect(Collectors.toList());
    }


    @Override
    public Optional<PatientResponseDTO> findById(Long patientId) {
        Optional<PatientEntity> optionalPatient = patientRepository.findByIdAndDeletedFalse(patientId);
        if(optionalPatient.isEmpty()){
            return Optional.empty();
        }
        return patientRepository.findByIdAndDeletedFalse(patientId).map(patientConverter::toResponseDTO);
    }

    @Override
    @Transactional
    public void deletePatient(Long patientId) {
        PatientEntity patient = patientRepository.findByIdAndDeletedFalse(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found or already deleted"));
        patient.setDeleted(true);
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(Long patientId, PatientUpdateDTO updatedData) {
        PatientEntity existingPatient = patientRepository.findByIdAndDeletedFalse(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // email dang dang nhap
        if (updatedData.getEmail().equals(currentUsername)) {
            throw new RuntimeException("Không thể dùng email trùng với tài khoản đang đăng nhập!");
        }

        if (userRepository.existsByUserName(updatedData.getEmail()) && !existingPatient.getEmail().equals(updatedData.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        String fullName = updatedData.getName();
        String email = updatedData.getEmail();
        String newPassword = updatedData.getPassword();

        // cap nhat vao table user va table patient
        UserEntity userEntity = existingPatient.getUser();
        userEntity.setUserName(email);
        // neu khong dien password
        if (newPassword == null || newPassword.isBlank()) {
            userEntity.setPassword(existingPatient.getUser().getPassword());
        } else {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(userEntity);

        existingPatient.setFullName(fullName);
        existingPatient.setEmail(email);
        existingPatient.setUser(userEntity);
        patientRepository.save(existingPatient);

        PatientResponseDTO patientResponseDTO = patientConverter.toResponseDTO(existingPatient);
        patientResponseDTO.setId(existingPatient.getId());
        return patientResponseDTO;
    }

    @Transactional
    @Override
    public PatientResponseDTO insertPatient(PatientUpdateDTO patientUpdateDTO) {
        if (userRepository.existsByUserName(patientUpdateDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại, không thể thêm mới!");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // email dang dang nhap
        if (patientUpdateDTO.getEmail().equals(currentUsername)) {
            throw new RuntimeException("Không thể dùng email trùng với tài khoản đang đăng nhập!");
        }

        PatientEntity patientEntity = new PatientEntity();
        UserEntity userEntity = new UserEntity();

        userEntity.setUserName(patientUpdateDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(patientUpdateDTO.getPassword()));
        userEntity.setStatus(1L);
        userRepository.save(userEntity);

        patientEntity.setFullName(patientUpdateDTO.getName());
        patientEntity.setEmail(patientUpdateDTO.getEmail());
        patientEntity.setUser(userEntity);
        patientRepository.save(patientEntity);

        RoleEntity defaultRole = roleRepository.findByRoleCode("ROLE_USER");
        userEntity.getRoles().add(defaultRole);
        userRepository.save(userEntity);

        return patientConverter.toResponseDTO(patientEntity);
    }

    @Transactional
    @Override
    public void restoreUser(Long id) {
        PatientEntity patientEntity = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        patientEntity.setDeleted(false);
    }


    @Override
    public UserEntity findByUserName(String userName) throws Exception {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new Exception("Cannot find user with username: " + userName));
    }

    @Override
    public PatientProfileDTO getPatientInfor(String username) {
        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại"));

        PatientEntity patient = user.getPatientEntity();
        if (patient == null || patient.getDeleted()) {
            throw new EntityNotFoundException("Patient không tồn tại hoặc đã bị xóa");
        }

        PatientProfileDTO dto = new PatientProfileDTO();
        dto.setId(patient.getId());
        dto.setFullname(patient.getFullName());
        dto.setBirth(patient.getBirth());
        dto.setGender(
                patient.getGender() != null
                        ? patient.getGender().name()
                        : null
        );
        dto.setAddress(patient.getAddress());
        dto.setPhone(patient.getPhone());
        dto.setCity(patient.getCity());
        dto.setDistrict(patient.getDistrict());
        dto.setEmail(patient.getEmail()); // readonly
        return dto;
    }

    @Transactional
    @Override
    public void updatePatientProfile(String username, PatientProfileDTO dto) {
        try {
            UserEntity user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại"));
            if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
                String hashed = passwordEncoder.encode(dto.getNewPassword());
                user.setPassword(hashed);
                userRepository.save(user);
            }

            PatientEntity patient = patientRepository.findByEmailAndDeletedFalse(username);
            patient.setFullName(dto.getFullname());
            patient.setBirth(dto.getBirth());
            patient.setGender(Gender.valueOf(dto.getGender()));
            patient.setAddress(dto.getAddress());
            patient.setPhone(dto.getPhone());
            patient.setCity(dto.getCity());
            patient.setDistrict(dto.getDistrict());
            patientRepository.save(patient);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật thông tin người dùng", e);
        }
    }


}
