package ptit.edu.vn.bookshop.service;

import ptit.edu.vn.bookshop.domain.dto.request.auth.ForgotPasswordRequestDTO;

public interface ResetPasswordService {
    String forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO);
    String otpVerification(String otp);
    String resetPassword(String newPassword, String confirmNewPassword, String resetToken);
}
