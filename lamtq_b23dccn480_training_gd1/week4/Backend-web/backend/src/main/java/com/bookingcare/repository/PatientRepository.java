package com.bookingcare.repository;

import com.bookingcare.entity.PatientEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity,Long> {
    List<PatientEntity> findAllByDeletedFalse();
    Optional<PatientEntity> findByIdAndDeletedFalse(Long id);
    boolean existsByEmail(String email);
    List<PatientEntity> findAllByDeletedTrue();
    PatientEntity findByEmailAndDeletedFalse(String email);
}
