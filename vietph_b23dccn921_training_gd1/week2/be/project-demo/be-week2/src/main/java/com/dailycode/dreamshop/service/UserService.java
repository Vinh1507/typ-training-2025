package com.dailycode.dreamshop.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dailycode.dreamshop.dto.DataResponse;
import com.dailycode.dreamshop.dto.LoginResponse;
import com.dailycode.dreamshop.dto.RegisterRequest;
import com.dailycode.dreamshop.model.User;
import com.dailycode.dreamshop.repository.UserRepository;
import com.dailycode.dreamshop.utils.error.AlreadyExistException;
import com.dailycode.dreamshop.utils.error.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public DataResponse getProfile(String email){
        User user= userRepository.findByEmail(email)
        .orElseThrow(()->new NotFoundException("User not found!"));
        LoginResponse userResponse= new LoginResponse(user.getId(), user.getEmail(), user.getRole() , user.getName(), user.getCreatedAt(), user.getUpdatedAt());
        
         return new DataResponse(HttpStatus.OK, true, null,userResponse);
    }
    public DataResponse craete(RegisterRequest request){
        Optional isExistUse= userRepository.findByEmail(request.getEmail());
        if(isExistUse.isPresent())
        {
            throw new AlreadyExistException("Email is used!");
        }
        User user= new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole()==null ? "USER" : "ADMIN");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        return new DataResponse(HttpStatus.CREATED, true, "User created!",null);
    }
}
