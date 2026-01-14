package com.bookingcare.controller;

import com.bookingcare.DTO.*;
import com.bookingcare.entity.AppointmentEntity;
import com.bookingcare.entity.SpecializationEntity;
import com.bookingcare.entity.UserEntity;
import com.bookingcare.repository.SpecializationRepository;
import com.bookingcare.service.IAdminService;
import com.bookingcare.service.IAppointmentService;
import com.bookingcare.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IAdminService adminService;
    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private IAppointmentService appointmentService;


    @GetMapping("/users")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        try {
            List<PatientResponseDTO> users = userService.findAllPatients();
            if (users == null || users.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi khi tải danh sách người dùng: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/users/deleted")
    public ResponseEntity<List<PatientResponseDTO>> getDeletedUsers() {
        try {
            List<PatientResponseDTO> users = userService.findDeletedPatients();
            if (users == null || users.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi khi tải danh sách người dùng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable("id") Long patientId) {
        return userService.findById(patientId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deletePatient(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.deletePatient(id);
            response.put("success", true);
            response.put("message", "Patient deleted successfully");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable("id") Long patientId,
                                           @Valid @RequestBody PatientUpdateDTO updateDTO) {
        try {
            PatientResponseDTO updated = userService.updatePatient(patientId, updateDTO);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            // email bi trung
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            // cac loi khac
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Lỗi hệ thống: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/users")
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientUpdateDTO updateDTO) {
        try {
            PatientResponseDTO patientResponseDTO = userService.insertPatient(updateDTO);
            return ResponseEntity.ok(patientResponseDTO);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.restoreUser(id);
            response.put("success", true);
            response.put("message", "Người dùng đã được khôi phục");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/users/{id}/promote")
    public ResponseEntity<?> promotePatientToDoctor(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.promotePatientToDoctor(id);
            response.put("success", true);
            response.put("message", "Đã cấp quyền bác sĩ cho tài khoản");
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", "Server error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/doctors")
    public ResponseEntity<?> getDoctors() {
        try {
            List<DoctorResponseDTO> listDoctor = adminService.getDoctors();
            if (listDoctor == null || listDoctor.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(listDoctor);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi khi tải danh sách bác sĩ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable("id") Long doctorId,
                                          @Valid @RequestBody DoctorUpdateDTO updateDTO) {
        try {
            DoctorResponseDTO updated = adminService.updateDoctor(doctorId, updateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "doctor", updated
            ));
        } catch (IllegalArgumentException e) {
            // email bi trung
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            // cac loi khac
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Lỗi hệ thống: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("id") Long doctorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.deleteDoctor(doctorId);
            response.put("success", true);
            response.put("message", "Xóa bác sĩ thành công");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/doctor")
    public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorUpdateDTO doctorUpdateDTO) {
        try {
            DoctorResponseDTO doctorResponseDTO = adminService.insertDoctor(doctorUpdateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "doctor", doctorResponseDTO
            ));
        } catch (IllegalArgumentException e) {
            // email bi trung
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            // cac loi khac
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Lỗi hệ thống: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/doctors/deleted")
    public ResponseEntity<?> getDeletedDoctors() {
        try {
            List<DoctorResponseDTO> listDoctor = adminService.getDoctorDeleted();
            if (listDoctor == null || listDoctor.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(listDoctor);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Lỗi khi tải danh sách bác sĩ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/doctors/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreDoctor(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.restoreDoctor(id);
            response.put("success", true);
            response.put("message", "Người dùng đã được khôi phục");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/specializations")
    public ResponseEntity<List<String>> getAllSpecializations() {
        return ResponseEntity.ok(adminService.findAllSpecializations());
    }

    @PostMapping("/specialization")
    public ResponseEntity<?> createSpecialization(@RequestBody @Valid SpecialUpdateDTO specialUpdateDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.addSpecialization(specialUpdateDTO.getSpecializationName());
            response.put("success", true);
            response.put("message", "Đã thêm chuyên khoa mới");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/specialization/{name}")
    public ResponseEntity<?> deleteSpecialization(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            adminService.deleteSpecialization(name);
            response.put("success", true);
            response.put("message", "Đã xóa chuyên khoa");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xóa chuyên khoa: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/bookings")
    public ResponseEntity<List<AppointmentResponseDTO>> getBookings() {
        List<AppointmentResponseDTO> responseDTOList = appointmentService.getAllBookings();
        return ResponseEntity.ok(responseDTOList != null ? responseDTOList : Collections.emptyList());
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        try {
            AppointmentResponseDTO appointmentResponseDTO = appointmentService.updateAppointment(id, appointmentRequestDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "appointment", appointmentResponseDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Lỗi hệ thống: " + e.getMessage()
            ));
        }
    }
}
