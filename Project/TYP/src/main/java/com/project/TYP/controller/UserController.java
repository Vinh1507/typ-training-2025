package com.project.TYP.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.project.TYP.entity.User;
import com.project.TYP.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.project.TYP.entity.ApiResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = this.userService.getUsers();
        var rs = new ApiResponse<>(HttpStatus.ACCEPTED, "Lấy người dùng thành công", users, null);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        User user = this.userService.getUserById(id);
        var rs = new ApiResponse<>(HttpStatus.ACCEPTED, "Lấy người dùng thành công", user, null);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        User newUser = this.userService.createUser(user);

        var result = new ApiResponse<User>(HttpStatus.CREATED, "Tạo người dùng thành công", newUser, null);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        User updateUser = this.userService.updateUser(id, user);
        var rs = new ApiResponse<>(HttpStatus.ACCEPTED, "Cập nhật người dùng thành công", updateUser, null);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        this.userService.deleteUserById(id);
        Long amount = this.userService.countUser();
        String str = "Còn lại : " + amount + " users";
        var rs = new ApiResponse<>(HttpStatus.ACCEPTED, "Xóa người dùng thành công", str, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(rs);
    }   
}
