package com.bookingcare.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class AppointmentResponseDTO {
    private Long id;

    // Thông tin lịch hẹn
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;
    private String status;
    private String note;
    private String reason;
    private Double totalCost;
    private LocalDateTime createdAt;

    // Người đặt lịch
    private String bookingName;
    private String bookingEmail;
    private String bookingPhone;

    // Bệnh nhân (lấy từ PatientEntity)
    private String name;
    private String gioitinh;
    private String namsinh;
    private String diachi;
    private String thanhpho;
    private String email;
    private String phone;

    // Bác sĩ (DoctorEntity)
    private String bacsi;
    private String department;
    private String diachikham;

    // Thông tin người dùng nếu có (User)
    private String emailUser;
}

