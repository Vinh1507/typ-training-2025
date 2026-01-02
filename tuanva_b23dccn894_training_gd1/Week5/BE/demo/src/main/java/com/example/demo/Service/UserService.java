package com.example.demo.Service;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.Duration;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final RedisService redis;
    private final ObjectMapper objectMapper;
    public UserService(UserRepository userRepo, RedisService redis, ObjectMapper objectMapper) {
        this.userRepo = userRepo;
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    @Value("${app.cache.ttl.user-seconds}")
    private int userTtl;

    @Value("${app.cache.ttl.null-seconds}")
    private int nullTtl;

    @Value("${app.cache.ttl.jitter-seconds}")
    private int jitter;


    private String key(Long id) { return "cache:user:" + id; }

    public User getUser(Long id) {
        String k = key(id);

        var cached = redis.get(k);
        if (cached.isPresent()) {
            Object v = cached.get();
            if (redis.isNullMarker(v)) return null;
            return objectMapper.convertValue(v, User.class);
        }

        // DB
        var user = userRepo.findById(id).orElse(null);
        if (user == null) {
            redis.setNull(k, Duration.ofSeconds(nullTtl), jitter); // cache penetration
            return null;
        }

        redis.set(k, user, Duration.ofSeconds(userTtl), jitter);
        return user;
    }

    public User updateUser(Long id, String name, String email) {
        var u = userRepo.findById(id).orElse(null);
        if (u == null) return null;

        u.setName(name);
        u.setEmail(email);
        var saved = userRepo.save(u);

        redis.del(key(id)); // cache invalidation
        return saved;
    }
}
