package com.project.TYP.service;

import java.util.List;

import com.project.TYP.entity.User;

public interface UserService {
    User createUser(User user);
    List<User> getUsers();
    User getUserById(Long id);
    void deleteUserById(Long id);
    User updateUser(Long id, User updateUser);
    Long countUser();
}
