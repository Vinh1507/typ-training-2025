package com.bookingcare.repository;

import com.bookingcare.entity.SpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecializationRepository extends JpaRepository<SpecializationEntity, Long> {
    Optional<SpecializationEntity> findByNameIgnoreCase(String name);
    void deleteSpecializationByName(String name);
    Optional<SpecializationEntity> findByName(String name);
}
