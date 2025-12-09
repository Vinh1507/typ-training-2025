package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> handleGetAllUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> handleGetUserById(Long id) {
        return this.userRepository.findById(id);
    }

    public User handleSaveUser(User newUser) {
        return this.userRepository.save(newUser);
    }

    public void handleDeleteUser(User user) {
        userRepository.delete(user);
    }

}
