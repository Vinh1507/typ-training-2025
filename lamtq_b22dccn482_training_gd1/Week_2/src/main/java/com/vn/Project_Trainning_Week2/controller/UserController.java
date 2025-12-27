package com.vn.Project_Trainning_Week2.controller;

import com.vn.Project_Trainning_Week2.dto.request.RequestDTO;
import com.vn.Project_Trainning_Week2.entity.User;
import com.vn.Project_Trainning_Week2.dto.response.ResponseDTO;
import com.vn.Project_Trainning_Week2.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController extends BaseController {

    IUserService userService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/get-list-user")
    public ResponseEntity<ResponseDTO<?>> getListUser(){
        log.info("Lấy danh sách User");
        List<User> list = userService.getListUser();
        ResponseDTO<Object> response = ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message("Lấy thành công danh sách User")
                .data(list).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-user-detail/{id}")
    public ResponseEntity<ResponseDTO<?>> getUserDetail(@PathVariable Long id){
        log.info(String.format("Lấy thông tin User có ID = %d",id));
        User userDetail = userService.getUserDetail(id);
        ResponseDTO<Object> response = ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message(String.format("Lấy thông tin User có ID = %d",id))
                .data(userDetail).build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/add-new-user")
    public ResponseEntity<ResponseDTO<?>> addNewUser(@RequestBody RequestDTO requestUserDTO){
        log.info("Thêm mới User");
        User newUser = userService.addNewUser(requestUserDTO);
        ResponseDTO<Object> response = ResponseDTO.builder()
                .code(HttpStatus.CREATED.value())
                .message("Thêm user thành công")
                .data(newUser).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<ResponseDTO<?>> updateUser(@RequestBody RequestDTO requestUserDTO, @PathVariable Long id){
        log.info(String.format("Cập nhật thông tin User với ID = %d", id));
        User updatedUser = userService.updateUser(requestUserDTO,id);
        ResponseDTO<Object> response = ResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .message(String.format("Cập nhật thông tin User với ID = %d", id))
                .data(updatedUser).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteUser(@PathVariable Long id){
        log.info(String.format("Xóa User với ID = %d",id));
        userService.deleteUser(id);
        ResponseDTO<Object> response = ResponseDTO.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message(String.format("Xóa User với ID = %d",id))
                .data(null).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
