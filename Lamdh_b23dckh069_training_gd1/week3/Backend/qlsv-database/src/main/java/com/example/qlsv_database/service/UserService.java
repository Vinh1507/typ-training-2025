package com.example.qlsv_database.service;

import com.example.qlsv_database.repository.UserRepository;
import com.example.qlsv_database.model.User;
import com.example.qlsv_database.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String newPassword, Role role){
        if(userRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(newPassword));
        u.setRole(role);
        return userRepository.save(u);
    }

}
