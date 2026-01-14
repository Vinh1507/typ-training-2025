package com.bookingcare.entity;

import com.bookingcare.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientEntity patientEntity;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @NotNull(message = "Doctor is required")
    private DoctorEntity doctorEntity;

    @Column(name = "date")
    @NotNull(message = "Appointment date is required")
    private LocalDate date;

    @Column(name = "time")
    @NotNull(message = "Appointment time is required")
    private LocalTime time;

    @Column(name = "note")
    private String note;

    @Column(name = "reason")
    @NotBlank(message = "Reason for appointment is required")
    private String reason;

    @Column(name = "total_cost")
    private Double totalCost;

    @Enumerated(EnumType.STRING)
    private Status status = Status.DANG_CHO;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "booking_name")
    private String bookingName;

    //thêm phần này
    @Column(name="payment_time")
    private LocalDateTime paymentTime;

    //thêm phần này
    @Column(name="transaction_id")
    private String transactionId;

    @Column(name = "booking_phone")
    private String bookingPhone;

    @Column(name = "booking_email")
    private String bookingEmail;

    // Tự động set createdAt khi insert
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}