package com.BTL_JAVA.BTL.DTO.Request.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UserUpdateRequest {

    private String userName;

    private String email;

    private String password;

    private String phoneNumber;

    private MultipartFile avatar;

    private List<String> roles;

}
