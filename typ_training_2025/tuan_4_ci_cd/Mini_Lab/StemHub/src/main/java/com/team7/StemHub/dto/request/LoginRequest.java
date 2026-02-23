package com.team7.StemHub.dto.request;
import lombok.Data;


@Data
public class LoginRequest {
    private String username;
    private String password;
}