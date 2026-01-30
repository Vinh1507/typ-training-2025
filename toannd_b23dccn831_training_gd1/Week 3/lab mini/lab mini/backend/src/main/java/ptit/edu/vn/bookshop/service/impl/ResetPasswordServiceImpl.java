package ptit.edu.vn.bookshop.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.auth.ForgotPasswordRequestDTO;
import ptit.edu.vn.bookshop.domain.entity.User;
import ptit.edu.vn.bookshop.exception.IdInvalidException;
import ptit.edu.vn.bookshop.repository.UserRepository;
import ptit.edu.vn.bookshop.service.EmailService;
import ptit.edu.vn.bookshop.service.RedisTokenService;
import ptit.edu.vn.bookshop.service.ResetPasswordService;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenService redisTokenService;

    public ResetPasswordServiceImpl(UserRepository userRepository,
                                    EmailService emailService,
                                    PasswordEncoder passwordEncoder,
                                    RedisTokenService redisTokenService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.redisTokenService = redisTokenService;
    }

    @Override
    @Transactional
    public String forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        User user = userRepository.findByEmail(forgotPasswordRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus().equals(StatusEnum.INACTIVE)) {
            throw new IllegalArgumentException("User account is not active");
        }

        // Xóa các token cũ nếu còn tồn tại
//        this.userTokenRepository.deleteByUserIdAndTokenType(user.getId(), TokenType.OTP);
        this.redisTokenService.deleteToken(user.getId().toString());

        // Tạo OTP mới
        String otp = String.format("%06d", new Random().nextInt(999999));

        this.redisTokenService.storeOtp(user.getId().toString(),otp, 120L);
        // Gửi email OTP
        Map<String, Object> variables = new HashMap<>();
        variables.put("otp", otp);
        emailService.sendEmailFromTemplateSync(
                user.getEmail(),
                "OTP for Password Reset",
                "otpVerify",
                variables
        );

        return "OTP has been sent to your email address. Please check your inbox.";
    }

    @Override
    public String otpVerification(String otp) {
        String userId = this.redisTokenService.getOtp(otp);
        if (userId == null) {
            throw new IdInvalidException("Invalid or expired OTP");
        }
        // Xóa OTP sau khi xác thực thành công
        this.redisTokenService.deleteOtp(otp);

        // Tạo ResetToken
        String resetTokenValue = UUID.randomUUID().toString();
        this.redisTokenService.storeResetToken(userId, resetTokenValue, 600L);
        return resetTokenValue;
    }

    @Override
    public String resetPassword(String newPassword, String confirmNewPassword, String resetTokenValue) {
        String userId = this.redisTokenService.getResetToken(resetTokenValue);

        if (userId == null) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new RuntimeException("New password and confirmation do not match");
        }
        // Lấy user
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IdInvalidException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        this.userRepository.save(user);

        this.redisTokenService.deleteResetToken(resetTokenValue);

        Map<String, Object> variables = new HashMap<>();
        variables.put("username", user.getName());
        emailService.sendEmailFromTemplateSync(
                user.getEmail(),
                "Password Changed Successfully",
                "resetPasswordNotifition",
                variables
        );

        return "Password has been reset successfully. An email confirmation has been sent.";
    }
}
