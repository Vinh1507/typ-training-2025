package ptit.edu.vn.bookshop.service;

import ptit.edu.vn.bookshop.domain.entity.RedisToken;

import java.util.concurrent.TimeUnit;

public interface RedisTokenService {

    void storeAccessToken(String accessToken, String userId);

    void storeRefreshToken(String refreshToken, String userId);

    void logout();

    String getAccessToken(String userId);

    String getRefreshToken(String userId);

    void deleteToken(String userId);

    void storeVerificationToken(String token, String userId, Long ttlSeconds);

    String getUserIdFromVerificationToken(String token);

    void deleteVerificationToken(String token);

   void storeOtp(String userId, String otp, long ttlSeconds) ;

    String getOtp(String otp);

    void deleteOtp(String otp) ;

    void storeResetToken(String userId, String token, long ttlSeconds);
     String getResetToken(String token);

    void deleteResetToken(String token);
}
