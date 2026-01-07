package com.example.qlsv_database.service;

import com.example.qlsv_database.repository.UserRepository;
import com.example.qlsv_database.model.User;
import com.example.qlsv_database.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    public User createUser(String username, String newPassword, Role role){
        if(userRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(newPassword));
        u.setRole(role);
        User savedUser = userRepository.save(u);

        // Cache sau khi tạo entity
        String cacheKey = "users: " + savedUser.getId();
        redisService.set(cacheKey, savedUser, 3600);

        return savedUser;
    }
    // Lấy user từ cache (cache first)
    public User getUser(Long userId){
        String cacheKey = "users: " + userId;

        // Kiểm tra cache
        User cached = (User) redisService.get(cacheKey);
        if(cached != null) return cached;

        // Nếu miss cache thì truy vấn database
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            redisService.set(cacheKey, null, 60);
            return null;
        }

        // cache với TTL + random để tránh cache avalanche
        long ttl = 3600 + (long) (Math.random() * 300);
        redisService.set(cacheKey, userOpt.get(), ttl);
        return userOpt.get();
    }

    // update user
    public User updateUser(User user){
        User saved = userRepository.save(user);

        // Invalidate cache
        String cacheKey = "users: " + user.getId();
        redisService.delete(cacheKey);

        // cache lại mới
        redisService.set(cacheKey, saved, 3600);
        return saved;
    }

    // delete user
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);

        String cacheKey = "users: " + userId;
        redisService.delete(cacheKey);
    }

    // Tìm theo username k dùng cache
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
