package com.oj.application.controller;

import com.oj.platform.components.iam.infrastructure.persistence.jpa.entity.UserJpa;
import com.oj.platform.components.iam.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.oj.platform.core.http.ApiResponse;
import com.oj.platform.core.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserJpaRepository userRepository;

    @GetMapping("/my-info")
    public ApiResponse<UserJpa> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserJpa user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ApiResponse.success(user, "Get user info successful", HttpStatus.OK);
    }
}
