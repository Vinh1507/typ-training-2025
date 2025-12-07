package com.example.demo.Service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.UserEntity;

public interface IUserService {
    UserEntity createUser(UserDTO userDTO) throws Exception;
    String login(String userName, String password) throws Exception;
    UserEntity findByUserName(String userName) throws Exception;
}
