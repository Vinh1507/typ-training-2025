package ptit.edu.vn.bookshop.service.impl;

import jakarta.transaction.Transactional;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.UserUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.auth.PasswordChangeRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.UserCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.LoginResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.UserPageResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.UserResponseDTO;
import ptit.edu.vn.bookshop.domain.entity.RedisToken;
import ptit.edu.vn.bookshop.domain.entity.Role;
import ptit.edu.vn.bookshop.domain.entity.User;
import ptit.edu.vn.bookshop.exception.IdInvalidException;

import ptit.edu.vn.bookshop.exception.UsernameNotFoundException;
import ptit.edu.vn.bookshop.repository.RedisTokenRepository;
import ptit.edu.vn.bookshop.repository.RoleRepository;
import ptit.edu.vn.bookshop.repository.UserRepository;
import ptit.edu.vn.bookshop.repository.specification.UserSpecificationBuilder;
import ptit.edu.vn.bookshop.service.EmailService;
import ptit.edu.vn.bookshop.service.UserService;
import ptit.edu.vn.bookshop.domain.dto.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.edu.vn.bookshop.util.security.SecurityUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final RedisTokenRepository redisTokenRepository;

    public UserServiceImpl(EmailService emailService, UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, UserMapper userMapper,
                             RedisTokenRepository redisTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.redisTokenRepository = redisTokenRepository;
    }

    @Value("${app.upload-file.base-path}")
    private String baseURI;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO) {
        // Kiểm tra email đã tồn tại
        if (userCreateRequestDTO.getEmail() != null && this.userRepository.existsByEmail(userCreateRequestDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        User user = this.userMapper.toEntity(userCreateRequestDTO);
        // kiem tra role
        Optional<Role> roleOptional = this.roleRepository.findById(userCreateRequestDTO.getRole().getId());
        if (!roleOptional.isPresent()) {
            throw new IdInvalidException("Role Id is invalid");
        }
        user.setRole(roleOptional.get());

        // Ma hoa password
        String encodedPassword = this.passwordEncoder.encode(userCreateRequestDTO.getPassword());
        user.setPassword(encodedPassword);

        return this.userMapper.toResponseDto(this.userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(UserUpdateRequestDTO userRequestDTO, Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new IdInvalidException("User not found");
        }
        User user = userOptional.get();

        if (userRequestDTO.getName() != null) user.setName(userRequestDTO.getName());
        if (userRequestDTO.getDateOfBirth() != null) user.setDateOfBirth(userRequestDTO.getDateOfBirth());
        if (userRequestDTO.getPhone() != null) user.setPhone(userRequestDTO.getPhone());
        if (userRequestDTO.getGender() != null) user.setGender(userRequestDTO.getGender());
        if (userRequestDTO.getStatus() != null) user.setStatus(userRequestDTO.getStatus());
        if (userRequestDTO.getAvatar() != null) user.setAvatar(userRequestDTO.getAvatar());
        return this.userMapper.toResponseDto(this.userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new IdInvalidException("User not found");
        }
        User user = userOptional.get();
        user.setStatus(StatusEnum.DELETED);
        this.userRepository.save(user);
    }


    @Override
    public UserResponseDTO fetchUser(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new IdInvalidException("User not found");
        }
        return this.userMapper.toResponseDto(userOptional.get());
    }

    @Override
    public UserPageResponseDTO fetchAllUsers(Pageable pageable, String[] user) {
        Page<User> userPage;
        if (user != null && user.length > 0) {
            UserSpecificationBuilder builder = new UserSpecificationBuilder(); // là 1 List chứa các specSearchCritetia
            for (String u : user) {
                Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
                Matcher matcher = pattern.matcher(u);
                if (matcher.find()) {
                    builder.with(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5)
                    );
                }
            }
            userPage = this.userRepository.findAll(builder.build(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        UserPageResponseDTO response = new UserPageResponseDTO();
        response.setPage(userPage.getNumber() + 1);
        response.setPageSize(userPage.getSize());
        response.setPages(userPage.getTotalPages());
        response.setTotal(userPage.getTotalElements());
        response.setUsers(userPage.getContent().stream().map(userMapper::toResponseDto).collect(Collectors.toList()));
        return response;
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        return this.userMapper.toResponseDto(user);
    }

    @Override
    public User getUserByUsername(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

//    @Override
//    public void updateUserToken(String token, String email) {
//        User user = this.userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
//        user.setRefreshToken(token);
//        this.userRepository.save(user);
//    }

    @Override
    public UserResponseDTO getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        RedisToken token = this.redisTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IdInvalidException("refresh token is invalid"));
        Long userId = token.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdInvalidException("User not found"));
        return userMapper.toResponseDto(user);
    }

    @Override
    public LoginResponseDTO.UserLogin getCurrentUserAccount() {
        String email = SecurityUtil.getCurrentUserLogin().orElseThrow(
                () -> new UsernameNotFoundException("User not found in security context")
        );
        User currentUserDB = this.getUserByUsername(email);

        if (currentUserDB == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        // Map Role -> UserRoleResponseDTO
        UserResponseDTO.UserRoleResponseDTO roleDTO = new UserResponseDTO.UserRoleResponseDTO(
                currentUserDB.getRole().getId(),
                currentUserDB.getRole().getName()
        );

        LoginResponseDTO.UserLogin userLogin = new LoginResponseDTO.UserLogin();
        userLogin.setId(currentUserDB.getId());
        userLogin.setEmail(currentUserDB.getEmail());
        userLogin.setName(currentUserDB.getName());
        userLogin.setStatus(currentUserDB.getStatus());
        userLogin.setRole(roleDTO);

        return userLogin;
    }

    @Override
    public void passwordChange(PasswordChangeRequestDTO passwordChangeRequestDTO, String email) {
        //check current password co map voi mat khau dang dang nhap khong qua token
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(passwordChangeRequestDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        if (!passwordChangeRequestDTO.getNewPassword().equals(passwordChangeRequestDTO.getConfirmNewPassword())) {
            throw new RuntimeException("New password and confirmation do not match");
        }
        user.setPassword(passwordEncoder.encode(passwordChangeRequestDTO.getNewPassword()));
        this.userRepository.save(user);
    }

}
