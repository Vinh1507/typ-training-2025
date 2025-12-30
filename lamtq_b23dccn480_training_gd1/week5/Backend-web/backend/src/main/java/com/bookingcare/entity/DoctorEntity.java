package com.bookingcare.entity;

import com.bookingcare.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "clinic_id", referencedColumnName = "id")
    private ClinicEntity clinic;

    @Column(name = "fullname", length = 100, nullable = false)
    private String fullName;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private SpecializationEntity special;

    @Column(name = "specialization")
    private String speciality;

    // Soft delete
    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted;

    @Column(name = "price")
    private Double price;
}
