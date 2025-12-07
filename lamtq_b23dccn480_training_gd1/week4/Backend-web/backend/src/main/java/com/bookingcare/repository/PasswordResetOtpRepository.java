package com.bookingcare.repository;

import com.bookingcare.entity.PasswordResetOTP;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOTP,String> {
    PasswordResetOTP findTopByEmailOrderByIdDesc(String email);
}
