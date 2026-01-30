package ptit.edu.vn.bookshop.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.edu.vn.bookshop.domain.constant.StatusEnum;
import ptit.edu.vn.bookshop.domain.dto.request.auth.RegisterRequestDTO;
import ptit.edu.vn.bookshop.domain.entity.Role;
import ptit.edu.vn.bookshop.domain.entity.User;
import ptit.edu.vn.bookshop.exception.UsernameNotFoundException;
import ptit.edu.vn.bookshop.repository.RoleRepository;
import ptit.edu.vn.bookshop.repository.UserRepository;
import ptit.edu.vn.bookshop.service.EmailService;
import ptit.edu.vn.bookshop.service.RedisTokenService;
import ptit.edu.vn.bookshop.service.RegisterService;
import ptit.edu.vn.bookshop.domain.dto.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenService redisTokenService;

    public RegisterServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                               UserMapper userMapper, EmailService emailService, PasswordEncoder passwordEncoder,
                               RedisTokenService redisTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.redisTokenService = redisTokenService;
    }

    @Override
    public String userRegister(RegisterRequestDTO registerRequestDTO) {
        String password = registerRequestDTO.getPassword();
        String confirmPassword = registerRequestDTO.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password and Confirm Password do not match");
        }

        Optional<User> userOptional = this.userRepository.findByEmail(registerRequestDTO.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (user.getStatus().equals(StatusEnum.ACTIVE)) {
                throw new IllegalArgumentException("Email is already registered and active");
            }
            // Nếu INACTIVE → update thông tin mới từ DTO
            user.setName(registerRequestDTO.getName());
            user.setPhone(registerRequestDTO.getPhone());
            user.setPassword(passwordEncoder.encode(password));
            this.userRepository.save(user);
            sendVerificationEmail(user);
            return "Email already exists but inactive. Information updated and verification email resent.";
        }

        // Nếu chưa tồn tại, tạo user mới
        user = this.userMapper.fromRegisterDto(registerRequestDTO);
        user.setStatus(StatusEnum.INACTIVE);
        user.setPassword(passwordEncoder.encode(password));

        Role role = this.roleRepository.findByName("USER")
                .orElseThrow(() -> new UsernameNotFoundException("Role not found"));
        user.setRole(role);

        this.userRepository.save(user);

        sendVerificationEmail(user);

        return "Registration successful. Please check your email to verify your account.";
    }


    private void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        // TTL 10 minutes
        this.redisTokenService.storeVerificationToken(token, user.getId().toString(), 600L);

        String verifyUrl = "http://localhost:8080/api/v1/auth/verify?token=" + token;
        Map<String, Object> variables = new HashMap<>();
        variables.put("confirmationLink", verifyUrl);
        this.emailService.sendEmailFromTemplateSync(
                user.getEmail(),
                "Please confirm account.",
                "registerConfirmation",
                variables
        );
    }

    @Override
    @Transactional
    public String verifyUser(String token) {
        String userId = this.redisTokenService.getUserIdFromVerificationToken(token);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid or expired verification token");
        }
        User user = this.userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getStatus().equals(StatusEnum.ACTIVE)) {
            return "Account already verified";
        }
        user.setStatus(StatusEnum.ACTIVE);
        this.userRepository.save(user);
        redisTokenService.deleteVerificationToken(token);
        return "Account verified successfully";
    }


}
