package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MetricController metricController;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        Timer.Sample sample = metricController.startTimer();
        try {
            metricController.incrementRequest();
            return ResponseEntity.ok(userService.getAllUsers());
        } finally {
            metricController.stopTimer(sample, "getAllUsers");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Timer.Sample sample = metricController.startTimer();
        try {
            metricController.incrementRequest();
            return userService.getUserById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } finally {
            metricController.stopTimer(sample, "getUserById");
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        Timer.Sample sample = metricController.startTimer();
        try {
            metricController.incrementRequest();
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } finally {
            metricController.stopTimer(sample, "createUser");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        Timer.Sample sample = metricController.startTimer();
        try {
            metricController.incrementRequest();
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } finally {
            metricController.stopTimer(sample, "updateUser");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Timer.Sample sample = metricController.startTimer();
        try {
            metricController.incrementRequest();
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } finally {
            metricController.stopTimer(sample, "deleteUser");
        }
    }
}
