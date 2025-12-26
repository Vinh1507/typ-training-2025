package com.example.qr_backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LoginTransaction implements Serializable {
    private String tx; // UUID
    private String username;
    private String status;  // WAITING, APPROVE, EXPIRED
    private long createdAt;
    private long expiredAt;

    public LoginTransaction(String username) {
        this.tx = java.util.UUID.randomUUID().toString();
        this.username = username;
        this.status = "WAITING";
        this.createdAt = System.currentTimeMillis();
        this.expiredAt = this.createdAt + 15000;    // 15 giây
    }

    @Override
    public String toString() {
        return "LoginTransaction{" +
                "tx='" + tx + '\'' +
                ", username='" + username + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
