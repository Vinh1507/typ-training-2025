package com.bookingcare.repository;
import com.bookingcare.DTO.DoctorResponseDTO;
import com.bookingcare.entity.DoctorEntity;
import com.bookingcare.entity.SpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
    List<DoctorEntity> findAllByIsDeletedFalse();
    boolean existsByEmail(String email);
    Optional<DoctorEntity> findByIdAndIsDeletedFalse(Long id);
    List<DoctorEntity> findAllByIsDeletedTrue();
    Long countBySpecial(SpecializationEntity specializationEntity);
    List<DoctorEntity> findAllBySpecial_NameAndIsDeletedFalse(String specializationName);
    Optional<DoctorEntity> findByEmailAndIsDeletedFalse(String email);
    DoctorEntity findDoctorEntityByEmailAndIsDeletedFalse(String email);
}