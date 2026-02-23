package com.team7.StemHub.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // 1. Kiểm tra xem có tham số 'redirect' gửi kèm không
        String targetUrl = request.getParameter("redirect");

        // 2. Nếu có, chuyển hướng người dùng đến đó
        if (targetUrl != null && !targetUrl.isEmpty()) {
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // 3. Nếu không, dùng cơ chế mặc định (về trang chủ hoặc trang bị chặn trước đó)
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}