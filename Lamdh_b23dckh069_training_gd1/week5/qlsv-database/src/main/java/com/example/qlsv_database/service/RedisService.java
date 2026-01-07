package com.example.qlsv_database.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //CRUD Cache
    public void set(String key, Object value, long ttlseconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlseconds));
    }
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // Bảng xếp hạng - Sorted set
    public void zAdd(String key, double score, String member){
        redisTemplate.opsForZSet().add(key, member, score);
    }
    public Set<ZSetOperations.TypedTuple<Object>> zRevRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }
}
