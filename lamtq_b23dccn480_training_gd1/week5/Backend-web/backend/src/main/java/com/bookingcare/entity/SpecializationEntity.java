package com.bookingcare.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "specialization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecializationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "special")
    private List<DoctorEntity> doctors;
}

