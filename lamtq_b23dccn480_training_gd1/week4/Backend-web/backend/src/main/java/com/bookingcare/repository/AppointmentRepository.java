package com.bookingcare.repository;

import com.bookingcare.entity.AppointmentEntity;
import com.bookingcare.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findAllByPatientEntity_Email(String email);
    List<AppointmentEntity> findAllByDoctorEntity_Id(Long id);
    List<AppointmentEntity> findAllByPatientEntity_EmailAndStatusNot(String email, Status status);
    Optional<AppointmentEntity> findByTransactionId(String transactionId);
}