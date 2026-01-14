package com.example.demo.Service.Impl;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.RoleEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.IUserService;
import com.example.demo.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UserEntity createUser(UserDTO userDTO) throws Exception {
        Optional<UserEntity> existingUser = userRepository.findByUserName(userDTO.getUserName());
        if (existingUser.isPresent()) {
            throw new Exception("Username already exists");
        }
        Optional<UserEntity> existingEmail = userRepository.findByEmail(userDTO.getEmail());
        if (existingEmail.isPresent()) {
            throw new Exception("Email already exists");
        }
        RoleEntity roleUser = roleRepository.findByRoleCode("ROLE_USER")
                .orElseThrow(() -> new Exception("Default role USER not found"));
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        UserEntity user = UserEntity.builder()
                .userName(userDTO.getUserName())
                .password(encodedPassword)
                .email(userDTO.getEmail())
                .deleted(false)
                .roles(List.of(roleUser))
                .build();
        return userRepository.save(user);
    }

    @Override
    public String login(String userName, String password) throws Exception {
        Optional<UserEntity> optionalUser = userRepository.findByUserName(userName);
        if (optionalUser.isEmpty()) {
            throw new Exception("Invalid username or password");
        }

        UserEntity userEntity = optionalUser.get();
        // check password
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException("Wrong phone number or password");
        }

        // load user ==> authentication token luu name, password real va authorities
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password, userEntity.getAuthorities());
        // tim bean authenticationManager de gan UsernamePasswordAuthenticationToken de xac thuc
        authenticationManager.authenticate(authenticationToken);
        // login thanh cong => sinh token
        return jwtTokenUtil.generateToken(userEntity); // token duoc sinh ra se duoc su dung de vao cac api, truoc khi vao cac api dung token vao websecurityConfig de xem quyen
    }

    @Override
    public UserEntity findByUserName(String userName) throws Exception {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new Exception("Cannot find user with username: " + userName));
    }
}
