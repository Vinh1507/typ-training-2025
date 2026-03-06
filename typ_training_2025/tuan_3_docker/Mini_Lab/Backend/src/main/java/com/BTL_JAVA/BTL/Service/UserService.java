package com.BTL_JAVA.BTL.Service;

import com.BTL_JAVA.BTL.DTO.Request.User.UserCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.User.UserUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.Security.PermissionResponse;
import com.BTL_JAVA.BTL.DTO.Response.Security.RoleResponse;
import com.BTL_JAVA.BTL.DTO.Response.User.UserResponse;
import com.BTL_JAVA.BTL.Entity.Permission;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.RoleRepository;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import com.BTL_JAVA.BTL.Service.Cloudinary.UploadImageFile;
import com.BTL_JAVA.BTL.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UploadImageFile uploadImageFile;

    public User createUser(UserCreationRequest request) throws IOException {

        User user = new User();
        if(userRepository.existsByFullName(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setFullName(request.getUsername());
       // user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        if(request.getAvatar() != null&&!request.getAvatar().isEmpty()){
            String url=uploadImageFile.uploadImage(request.getAvatar());
            user.setAvatar(url);
        }

        HashSet<String> roles = new HashSet<String>();
        roles.add(Role.USER.toString());
//        user.setRoles(roles);

        return userRepository.save(user);
    }

    public UserResponse getMyInfo(){
       var context = SecurityContextHolder.getContext();
       String name=context.getAuthentication().getName();
       User user =userRepository.findByFullName(name).orElseThrow(
               ()->new AppException(ErrorCode.USER_NOT_EXISTED)
       );
       UserResponse userResponse = new UserResponse();
       userResponse.setId(user.getId());
       userResponse.setUserName(user.getFullName());
       userResponse.setEmail(user.getEmail());
       userResponse.setPhoneNumber(user.getPhoneNumber());
       userResponse.setAvatar(user.getAvatar());

        Set<RoleResponse> roleResponses = user.getRoles().stream()
                .map(role -> {
                    RoleResponse resp = new RoleResponse();
                    resp.setName(role.getNameRoles());
                    resp.setDescription(role.getDescription());
                    // Map Set<Permission> -> Set<PermissionResponse>
                    Set<PermissionResponse> permDtos = (role.getPermissions() == null ? java.util.Set.<Permission>of() : role.getPermissions())
                            .stream()
                            .map(p -> {
                                PermissionResponse pr = new PermissionResponse();
                                pr.setName(p.getNamePermission());   // đổi theo field thực tế của Permission
                                pr.setDescription(p.getDescription());
                                return pr;
                            })
                            .collect(java.util.stream.Collectors.toSet());

                    resp.setPermissions(permDtos);
                    return resp;
                })
                .collect(Collectors.toSet());

        userResponse.setRoles(roleResponses);

       return userResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostAuthorize("returnObject.fullName=authentication.name")
    public UserResponse getUser(int id){
        User user= userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found!"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setUserName(user.getFullName());
        userResponse.setAvatar(user.getAvatar());
        userResponse.setEmail(user.getEmail());

        return userResponse;
    }

    public UserResponse  updateUser(int id, UserUpdateRequest request) throws IOException {
        User user=userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        UserResponse userResponse = new UserResponse();
        if(request.getUserName()!=null)  user.setFullName(request.getUserName());
        if(request.getPassword()!=null) user.setPassword(request.getPassword());
        if(request.getEmail()!=null) user.setEmail(request.getEmail());
        if(request.getPhoneNumber()!=null) user.setPhoneNumber(request.getPhoneNumber());

        if(request.getAvatar()!=null&&!request.getAvatar().isEmpty()){
            String url=uploadImageFile.uploadImage(request.getAvatar());
            user.setAvatar(url);
        }

        if(request.getPassword()!=null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            var roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }

        userRepository.save(user);

        userResponse.setId(user.getId());
        userResponse.setUserName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setAvatar(user.getAvatar());
        Set<RoleResponse> roleResponses = user.getRoles().stream()
                .map(role -> {
                    RoleResponse resp = new RoleResponse();
                    resp.setName(role.getNameRoles());
                    resp.setDescription(role.getDescription());
                    // Map Set<Permission> -> Set<PermissionResponse>
                    Set<PermissionResponse> permDtos = (role.getPermissions() == null ? java.util.Set.<Permission>of() : role.getPermissions())
                            .stream()
                            .map(p -> {
                                PermissionResponse pr = new PermissionResponse();
                                pr.setName(p.getNamePermission());   // đổi theo field thực tế của Permission
                                pr.setDescription(p.getDescription());
                                return pr;
                            })
                            .collect(java.util.stream.Collectors.toSet());

                    resp.setPermissions(permDtos);
                    return resp;
                })
                .collect(Collectors.toSet());

        userResponse.setRoles(roleResponses);


        return userResponse;
    }

    public void deleteUser(int id){
        userRepository.deleteById(id);
    }
}
