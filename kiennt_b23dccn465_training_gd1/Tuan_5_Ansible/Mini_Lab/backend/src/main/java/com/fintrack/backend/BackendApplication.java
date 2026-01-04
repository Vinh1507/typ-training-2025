package com.fintrack.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3002", "http://localhost:5173"})  // ✅ Cho phép FE React gọi từ nhiều port
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
