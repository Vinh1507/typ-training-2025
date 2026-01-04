package com.fintrack.backend.auth.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // SỬA: Map vào cột 'full_name' trong SQL, bắt buộc có dữ liệu
    @NotBlank
    @Column(name = "full_name", nullable = false)
    private String fullName;

    // SỬA: Map vào cột 'email', không được null
    @Email
    @NotBlank
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    // SỬA: Map vào cột 'phone', có thể null
    @Column(name = "phone")
    private String phone;

    // SỬA: Map vào cột 'password', không được null
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    // Đã đúng với SQL mới
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    // Đã đúng với SQL mới
    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    // --- Các phương thức của UserDetails (GIỮ NGUYÊN) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
