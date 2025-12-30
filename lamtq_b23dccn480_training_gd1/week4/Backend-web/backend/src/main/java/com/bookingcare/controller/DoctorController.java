package com.bookingcare.controller;

import com.bookingcare.DTO.*;
import com.bookingcare.entity.UserEntity;
import com.bookingcare.service.IAdminService;
import com.bookingcare.service.IAppointmentService;
import com.bookingcare.service.IDoctorService;
import com.bookingcare.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    @Autowired
    private IAdminService  adminService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAppointmentService appointmentService;

    @Autowired
    private IDoctorService doctorService;

    @GetMapping
    public ResponseEntity<?> getDoctorsBySpecialization(@RequestParam String specialization) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DoctorDTO> doctors = doctorService.findDoctorBySpecialization(specialization);
            response.put("success", true);
            response.put("doctors", doctors);
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/bookings-doctor")
    public ResponseEntity<?> getBookingByDoctor(@AuthenticationPrincipal UserEntity user) {
        try{
            String username = user.getUsername();
            Long idBacSi = userService.findByUserName(username).getDoctorEntity().getId();

            List<AppointmentResponseDTO> bookings = appointmentService.getAppointmentByDoctorId(idBacSi);
            return ResponseEntity.ok(bookings);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy danh sách lịch khám"));
        }
    }

    @PutMapping("/bookings-status/{id}")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id) {
        try {
            appointmentService.updateBookingStatus(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cập nhật trạng thái thành công"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Cập nhật thất bại"));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getDoctorProfile(@AuthenticationPrincipal UserEntity user) {
        try {
            String username = user.getUsername();
            DoctorProfileDTO doctorInfor = doctorService.getDoctorInfor(username);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "doctor", doctorInfor
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Không thể lấy thông tin bác sĩ"));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateDoctorProfile(@RequestBody DoctorUpdateProfileDTO doctorUpdateProfileDTO, @AuthenticationPrincipal UserEntity user) {
        try{
            String userName  = user.getUsername();
            doctorService.updateDoctorProfile(doctorUpdateProfileDTO, userName);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cập nhật thông tin bác sĩ thành công"
            ));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Không thể cập nhật thông tin bác sĩ"));
        }
    }

}
