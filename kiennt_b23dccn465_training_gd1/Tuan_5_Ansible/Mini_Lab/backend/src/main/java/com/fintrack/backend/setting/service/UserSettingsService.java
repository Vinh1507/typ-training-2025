package com.fintrack.backend.setting.service;

import com.fintrack.backend.auth.models.User;
import com.fintrack.backend.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Lấy thông tin user hiện tại từ authentication
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Trả về email
    }

    // Lấy thông tin profile
    public Map<String, Object> getUserProfile() {
        String email = getCurrentUserEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.getId());
        profile.put("fullName", user.getFullName());
        profile.put("email", user.getEmail());
        profile.put("phone", user.getPhone());

        return profile;
    }

    // Cập nhật thông tin profile
    @Transactional
    public Map<String, Object> updateUserProfile(Map<String, String> updateData) {
        String email = getCurrentUserEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        if (updateData.containsKey("fullName")) {
            user.setFullName(updateData.get("fullName"));
        }
        if (updateData.containsKey("phone")) {
            user.setPhone(updateData.get("phone"));
        }

        User updatedUser = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("user", Map.of(
                "id", updatedUser.getId(),
                "fullName", updatedUser.getFullName(),
                "email", updatedUser.getEmail(),
                "phone", updatedUser.getPhone()
        ));

        return response;
    }

    // Đổi mật khẩu
    @Transactional
    public Map<String, Object> changePassword(Map<String, String> passwordData) {
        String email = getCurrentUserEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        String currentPassword = passwordData.get("currentPassword");
        String newPassword = passwordData.get("newPassword");
        String confirmPassword = passwordData.get("confirmPassword");

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Mật khẩu hiện tại không đúng");
            return error;
        }

        // Kiểm tra mật khẩu mới và xác nhận
        if (!newPassword.equals(confirmPassword)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Mật khẩu mới và xác nhận không khớp");
            return error;
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Mật khẩu đã được thay đổi thành công");

        return response;
    }
}