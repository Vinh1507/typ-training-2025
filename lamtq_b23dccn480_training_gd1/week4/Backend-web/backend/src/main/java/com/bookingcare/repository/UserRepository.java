package com.bookingcare.repository;

import com.bookingcare.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);
    boolean existsByUserName(String userName);
    Optional<UserEntity> findByUserNameAndDoctorEntity_IsDeletedFalse(String username);
    Optional<UserEntity> findByUserNameAndPatientEntity_DeletedFalse(String username);
}
