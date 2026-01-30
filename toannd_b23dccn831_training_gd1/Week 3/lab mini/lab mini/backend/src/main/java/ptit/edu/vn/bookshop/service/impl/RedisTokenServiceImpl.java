package ptit.edu.vn.bookshop.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ptit.edu.vn.bookshop.domain.entity.RedisToken;
import ptit.edu.vn.bookshop.domain.entity.User;
import ptit.edu.vn.bookshop.exception.UsernameNotFoundException;
import ptit.edu.vn.bookshop.repository.RedisTokenRepository;
import ptit.edu.vn.bookshop.service.RedisTokenService;
import ptit.edu.vn.bookshop.service.UserService;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenServiceImpl implements RedisTokenService {

    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String ACCESS_PREFIX = "ACCESS_TOKEN:";
    private static final String REFRESH_PREFIX = "REFRESH_TOKEN:";
    private static final String RESET_PREFIX = "RESET_TOKEN:";
    private static final String OTP_PREFIX = "OTP_TOKEN:";
    private static final String VERIFY_PREFIX = "VERIFY_TOKEN:";

    @Value("${app.jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    @Value("${app.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    public RedisTokenServiceImpl(RedisTemplate<String, String> redisTemplate, UserService userService) {
        this.redisTemplate = redisTemplate;
        this.userService = userService;
    }

    @Override
    public void storeAccessToken(String accessToken, String userId) {
        this.redisTemplate.opsForValue().set(ACCESS_PREFIX + userId, accessToken, accessTokenExpiration, TimeUnit.SECONDS);
    }

    @Override
    public void storeRefreshToken(String refreshToken, String userId) {
        this.redisTemplate.opsForValue().set(REFRESH_PREFIX + userId, refreshToken, refreshTokenExpiration, TimeUnit.SECONDS);
    }

    @Override
    public void logout() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User user = this.userService.getUserByUsername(email);
        deleteToken(user.getId().toString());
    }

    @Override
    public String getAccessToken(String userId) {
        return this.redisTemplate.opsForValue().get(ACCESS_PREFIX + userId);
    }

    @Override
    public String getRefreshToken(String userId) {
        return this.redisTemplate.opsForValue().get(REFRESH_PREFIX + userId);
    }

    @Override
    public void deleteToken(String userId) {
        this.redisTemplate.delete(ACCESS_PREFIX + userId);
        this.redisTemplate.delete(REFRESH_PREFIX + userId);
    }

    @Override
    public void storeVerificationToken(String token, String userId, Long ttlSeconds) {
        this.redisTemplate.opsForValue().set(VERIFY_PREFIX + token, userId, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public String getUserIdFromVerificationToken(String token) {
        return this.redisTemplate.opsForValue().get(VERIFY_PREFIX + token);
    }

    @Override
    public void deleteVerificationToken(String token) {
        this.redisTemplate.delete(VERIFY_PREFIX + token);
    }

    public void storeOtp(String userId, String otp, long ttlSeconds) {
        redisTemplate.opsForValue().set(OTP_PREFIX + otp, userId, ttlSeconds, TimeUnit.SECONDS);
    }

    public String getOtp(String otp) {
        return redisTemplate.opsForValue().get(OTP_PREFIX + otp);
    }

    public void deleteOtp(String otp) {
        redisTemplate.delete(OTP_PREFIX + otp);
    }

    public void storeResetToken(String userId, String token, long ttlSeconds) {
        redisTemplate.opsForValue().set(RESET_PREFIX + token, userId, ttlSeconds, TimeUnit.SECONDS);
    }

    public String getResetToken(String token) {
        return redisTemplate.opsForValue().get(RESET_PREFIX + token);
    }

    public void deleteResetToken(String token) {
        redisTemplate.delete(RESET_PREFIX + token);
    }

}
