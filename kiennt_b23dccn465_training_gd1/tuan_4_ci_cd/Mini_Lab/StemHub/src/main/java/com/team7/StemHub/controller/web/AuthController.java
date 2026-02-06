package com.team7.StemHub.controller.web;

import com.team7.StemHub.dto.request.SignupRequest;
import com.team7.StemHub.exception.RegistrationException;
import com.team7.StemHub.facade.AuthFacade;
import jakarta.servlet.http.HttpServletRequest; // <--- Nhớ import cái này
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthFacade authFacade;

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "logout", required = false) String logout,
                        HttpServletRequest request, // 1. Thêm tham số này để lấy thông tin request
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
        }
        if (logout != null) {
            model.addAttribute("message", "Bạn đã đăng xuất thành công.");
        }

        String referer = request.getHeader("Referer");

        if (referer != null && !referer.contains("/auth/login") && !referer.contains("/auth/register")) {
            model.addAttribute("previousUrl", referer);
        }

        return "auth/enter";
    }

    @GetMapping({"/signup", "/register"})
    public String showSignupForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute SignupRequest dto, Model model) {
        try {
            authFacade.register(dto);
            model.addAttribute("message", "Đăng ký thành công. Vui lòng đăng nhập.");
            return "auth/enter";
        } catch (RegistrationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", dto.getUsername());
            model.addAttribute("email", dto.getEmail());
            model.addAttribute("fullname", dto.getFullname());
            return "auth/register";
        }
    }
}