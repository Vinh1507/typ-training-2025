package com.example.demo.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RedisService {
    private static final String NULL_MARKER = "__NULL__";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<Object> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void set(String key, Object value, Duration ttl, int jitterSeconds) {
        Duration finalTtl = ttl.plusSeconds(ThreadLocalRandom.current().nextInt(0, jitterSeconds + 1));
        redisTemplate.opsForValue().set(key, value, finalTtl);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void setNull(String key, Duration ttl, int jitterSeconds) {
        set(key, NULL_MARKER, ttl, jitterSeconds);
    }

    public boolean isNullMarker(Object value) {
        return (value instanceof String s) && NULL_MARKER.equals(s);
    }
}
