package com.example.qr_backend.service;

import com.example.qr_backend.entity.LoginTransaction;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    // Spring Boot tự động cấu hình RedisTemplate này
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final String KEY_PREFIX = "qr_tx:";

    //Lưu transaction vào Redis với TTL 15s
    public void saveTransaction(LoginTransaction tx) {
        String key = KEY_PREFIX + tx.getTx();
        redisTemplate.opsForValue().set(key, tx, 15, TimeUnit.SECONDS);
    }

    public LoginTransaction getTransaction(String txId) {
        String key = KEY_PREFIX + txId;
        return (LoginTransaction) redisTemplate.opsForValue().get(key);
    }

    public void updateTransaction(LoginTransaction tx) {
        String key = KEY_PREFIX + tx.getTx();
        long remainingTTL = tx.getExpiredAt() - System.currentTimeMillis();
        if(remainingTTL <= 0) {
            redisTemplate.delete(key);
        } else {
            redisTemplate.opsForValue().set(key, tx, remainingTTL, TimeUnit.SECONDS);
        }
    }
}
