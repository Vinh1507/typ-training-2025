package com.example.demo.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> handleGetAllUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> handleGetUserById(Long id) {
        String key = "user:" + id;
        // Kiểm tra cache
        Object cachedUser = redisService.get(key);

        if (cachedUser != null) {
            // Nếu cache trả về đánh dấu "không tìm thấy" -> return rỗng luôn, ko chọc DB
            if (cachedUser instanceof String && "USER_NOT_FOUND".equals(cachedUser)) {
                System.out.println("CACHE HIT (Penetration Protection): Bỏ qua DB cho ID " + id);
                return Optional.empty();
            }
            // Nếu là User thật -> return
            if (cachedUser instanceof User) {
                System.out.println("CACHE HIT (Success): Lấy User từ Redis cho ID " + id);
                return Optional.of((User) cachedUser);
            }
        }

        System.out.println("CACHE MISS: Đang truy vấn Database cho ID " + id);
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            redisService.setWithTTL(key, user.get(), 10, TimeUnit.MINUTES);
        } else {
            // Cơ chế bảo vệ Cache Penetration:
            // Nếu không tìm thấy trong DB, lưu đánh dấu vào cache với TTL ngắn, vd để TTL=1
            // phút
            // => Để các request sau ko chọc vào DB nữa.
            redisService.setWithTTL(key, "USER_NOT_FOUND", 1, TimeUnit.MINUTES);
        }
        return user;
    }

    public User handleSaveUser(User newUser) {
        User savedUser = this.userRepository.save(newUser);
        // Xóa cache khi thêm user mới
        redisService.delete("user:" + savedUser.getId());
        return savedUser;
    }

    public void handleDeleteUser(User user) {
        redisService.delete("user:" + user.getId());
        userRepository.delete(user);
    }

}
