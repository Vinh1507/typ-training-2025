package com.team7.StemHub.facade;

import com.team7.StemHub.dto.request.SignupRequest;
import com.team7.StemHub.exception.RegistrationException;
import com.team7.StemHub.model.User;
import com.team7.StemHub.model.enums.Role;
import com.team7.StemHub.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;

    public void register(SignupRequest dto) throws RegistrationException {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RegistrationException("Mật khẩu xác nhận không khớp");
        }
        if (authService.findByUsername(dto.getUsername()) != null) {
            throw new RegistrationException("Tên đăng nhập đã tồn tại");
        }
        if (authService.findByEmail(dto.getEmail()) != null) {
            throw new RegistrationException("Email đã tồn tại");
        }
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .fullname(dto.getFullname())
                .password(dto.getPassword())
                .role(Role.USER)
                .build();
        authService.register(user);
    }
}