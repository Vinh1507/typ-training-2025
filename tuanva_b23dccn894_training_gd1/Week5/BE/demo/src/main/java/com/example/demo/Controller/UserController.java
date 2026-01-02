package com.example.demo.Controller;

import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        User u = userService.getUser(id);
        if (u == null) return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        return ResponseEntity.ok(Map.of("data", u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User u = userService.updateUser(id, body.get("name"), body.get("email"));
        if (u == null) return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        return ResponseEntity.ok(Map.of("message", "Updated. Cache invalidated.", "data", u));
    }
}
