package com.BTL_JAVA.BTL.Controller;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.User.UserCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.User.UserUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.Security.PermissionResponse;
import com.BTL_JAVA.BTL.DTO.Response.Security.RoleResponse;
import com.BTL_JAVA.BTL.DTO.Response.User.UserResponse;
import com.BTL_JAVA.BTL.Entity.Permission;
import com.BTL_JAVA.BTL.Entity.Role;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<User> createUser(@ModelAttribute @Valid UserCreationRequest request) throws IOException {
        ApiResponse<User> response = new ApiResponse<>();

        response.setResult(userService.createUser(request));

        return response;
    }
    @GetMapping()
     List<UserResponse> getAllUsers() {
        // [1] Lấy data entity
        List<User> users = userService.getAllUsers();

        // [2] Khai báo kết quả trả về
        List<UserResponse> result = new ArrayList<>();

        // [3] Duyệt từng User và map sang UserResponse
        for (User u : users) {
            // 3.1 Map các field cơ bản của User
            UserResponse ur = new UserResponse();
            ur.setId(u.getId());
            ur.setUserName(u.getFullName());
            ur.setEmail(u.getEmail());
            ur.setPhoneNumber(u.getPhoneNumber());

            // 3.2 Map roles của User
            Set<RoleResponse> roleDtos = new LinkedHashSet<>();
            Set<Role> roles = (u.getRoles() != null) ? u.getRoles() : java.util.Set.of();

            for (Role r : roles) {
                RoleResponse rr = new RoleResponse();
                rr.setName(r.getNameRoles());
                rr.setDescription(r.getDescription());

                // 3.3 Map permissions của từng Role
                Set<PermissionResponse> permDtos = new LinkedHashSet<>();
                Set<Permission> perms = (r.getPermissions() != null) ? r.getPermissions() : java.util.Set.of();

                for (Permission p : perms) {
                    PermissionResponse pr = new PermissionResponse();
                    pr.setName(p.getNamePermission());     // đổi đúng tên getter của bạn
                    pr.setDescription(p.getDescription());
                    permDtos.add(pr);
                }

                // 3.4 Gắn permissions vào role DTO
                rr.setPermissions(permDtos);

                // 3.5 Thêm role DTO vào tập roleDtos
                roleDtos.add(rr);
            }

            // 3.6 Gắn roles vào user DTO
            ur.setRoles(roleDtos);

            // 3.7 Thêm user DTO vào kết quả
            result.add(ur);
        }

        // [4] Trả kết quả
        return result;
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") int userId){
       return userService.getUser(userId);
    }

    @GetMapping("/myInfor")
    UserResponse getMyinfor(){
        return userService.getMyInfo();
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse updateUser(
            @PathVariable("userId") int userId,
            @ModelAttribute UserUpdateRequest request
    ) throws IOException {
        return userService.updateUser(userId, request);
    }


    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") int userId){
          userService.deleteUser(userId);
          return "User Deleted Successfully";
    }
}
