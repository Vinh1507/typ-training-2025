package com.example.demo.DTO;

public class TokenValidationResponse {
    private boolean valid;
    private String message;
    private Long userId;
    private String email;
    private String role;

    public TokenValidationResponse() {
    }

    public TokenValidationResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public TokenValidationResponse(boolean valid, String message, Long userId, String email, String role) {
        this.valid = valid;
        this.message = message;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

