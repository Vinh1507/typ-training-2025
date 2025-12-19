package com.project.TYP.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.TYP.entity.User;
import com.project.TYP.repository.UserRepository;
import com.project.TYP.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email này đã tồn tại");
        }
        return this.userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        try {
            return this.userRepository.findById(id).get();
        } catch (Exception e) {
            throw new EntityNotFoundException("Người dùng không tồn tại");
        } 
    }

    @Override
    public void deleteUserById(Long id) {
        if (this.userRepository.existsById(id)) {
            this.userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Người dùng không tồn tại");
        }
    }

    @Override
    public User updateUser(Long id, User updateUser) {
        return this.userRepository.findById(id)
        .map(user -> {
            if (userRepository.existsByEmail(updateUser.getEmail()) 
                && !user.getEmail().equals(updateUser.getEmail())) {
                throw new IllegalArgumentException("Email đã tồn tại");
            }
            user.setEmail(updateUser.getEmail());
            user.setUsername(updateUser.getUsername());
            user.setPassword(updateUser.getPassword());
            return userRepository.save(user);
        }).orElseThrow(() -> new EntityNotFoundException("Người dùng không tồn tại"));
    }

    @Override
    public Long countUser() {
        return this.userRepository.countBy();
    }
}
