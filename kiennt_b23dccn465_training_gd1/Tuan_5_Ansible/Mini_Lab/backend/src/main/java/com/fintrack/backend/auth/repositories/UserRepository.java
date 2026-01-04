package com.fintrack.backend.auth.repositories;

import com.fintrack.backend.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Phương thức để tìm người dùng bằng email
    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);
}