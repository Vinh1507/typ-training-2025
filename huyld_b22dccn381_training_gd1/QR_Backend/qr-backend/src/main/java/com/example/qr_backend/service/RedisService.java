package com.example.qr_backend.service;

import com.example.qr_backend.entity.LoginTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
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
        // 1. Lưu từng field
        redisTemplate.opsForHash().put(key, "tx", tx.getTx());
        redisTemplate.opsForHash().put(key, "username", tx.getUsername());
        redisTemplate.opsForHash().put(key, "status", "WAITING");

        // 2. Set TTL 15s
        redisTemplate.expire(key, Duration.ofSeconds(15));

        System.out.println(key);
    }

    public LoginTransaction getTransaction(String txId) {
        String key = KEY_PREFIX + txId;

        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            return null;
        }

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        LoginTransaction tx = new LoginTransaction();
        tx.setTx((String) entries.get("tx"));
        tx.setUsername((String) entries.get("username"));
        tx.setStatus((String) entries.get("status"));

        return tx;
    }
}
