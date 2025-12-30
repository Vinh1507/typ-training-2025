package com.bookingcare.controller;

import com.bookingcare.DTO.*;
import com.bookingcare.entity.UserEntity;
import com.bookingcare.service.IAppointmentService;
import com.bookingcare.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IAppointmentService appointmentService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(Map.of("errors", errorMessages));
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Mật khẩu nhập lại không khớp"));
            }
            UserEntity userEntity = userService.createUser(userDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully");
            response.put("id", userEntity.getId()); // Thêm ID của user vừa tạo
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            UserEntity user = userService.findByUserName(userDTO.getUserName());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Tài khoản không tồn tại"));
            }
            String token = userService.login(userDTO.getUserName(), userDTO.getPassword());
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Sai mật khẩu"));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng nhập thành công");
            response.put("token", token);
            response.put("email", user.getUsername());
            response.put("id", user.getId());
            String role = user.getRoles().isEmpty() ? "ROLE_USER" : user.getRoles().get(0).getRoleCode();
            response.put("role", role);
            user.getRoles().stream()
                    .filter(r -> "ROLE_DOCTOR".equals(r.getRoleCode()))
                    .findFirst()
                    .ifPresent(r -> {
                        if (user.getDoctorEntity() != null) {
                            response.put("idbacsi", user.getDoctorEntity().getId());
                            response.put("majorName", user.getDoctorEntity().getSpecial().getName());
                        }
                    });

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email không được để trống"));
        }

        try {
            userService.sendOtp(email);
            return ResponseEntity.ok(Map.of("message", "OTP đã được gửi đến email của bạn"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Email không tồn tại"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        boolean isValid = userService.verifyOtp(email, otp);

        if (!isValid) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "OTP không hợp lệ hoặc đã hết hạn"));
        }

        return ResponseEntity.ok(Map.of("message", "Xác thực OTP thành công"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody Map<String, String> request){
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        if(newPassword == null || newPassword.length() < 6){
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Mật khẩu mới không hợp lệ!"));
        }
        userService.setNewPassword(email, newPassword);
        return ResponseEntity.ok(Map.of("message", "Mật khẩu đã được thay đổi"));
    }



    
    @PostMapping("/booking")
    public ResponseEntity<?> createApointment(@Valid @RequestBody BookingDTO bookingDTO, @AuthenticationPrincipal UserEntity currentUser) {
        try{
            if (bookingDTO.getNguoiDatLichTen() != null && bookingDTO.getNguoiDatLichTen().length() > 0) {
                if (bookingDTO.getEmail() != null && bookingDTO.getEmail().equals(currentUser.getUsername())) {
                    throw new IllegalArgumentException("Email của bệnh nhân không được trùng với email của tài khoản hiện tại!");
                }
            }
            appointmentService.createAppointment(bookingDTO, currentUser.getUsername());
            return ResponseEntity.ok(Map.of("message", "Đặt lịch thành công"));
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<AppointmentResponseDTO>> getUserBookings(@AuthenticationPrincipal UserEntity currentUser) {
        String username = currentUser.getUsername(); // hoặc currentUser.getPatientEntity().getEmail() nếu cần
        List<AppointmentResponseDTO> bookings = appointmentService.getAppointmentByUsername(username);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id, @AuthenticationPrincipal UserEntity currentUser) {
        try{
            appointmentService.deleteAppointment(id, currentUser.getUsername());
            return ResponseEntity.ok(Map.of("message", "Đã xóa lịch hẹn"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getPatientProfile(@AuthenticationPrincipal UserEntity user) {
        try {
            String username = user.getUsername();
            PatientProfileDTO patientInfor = userService.getPatientInfor(username);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "patient", patientInfor
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Không thể lấy thông tin người dùng"));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal UserEntity user, @RequestBody PatientProfileDTO dto) {
        try {
            userService.updatePatientProfile(user.getUsername(), dto);

            return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật thông tin thành công"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("success", false, "message", "Lỗi server"));
        }
    }
}

