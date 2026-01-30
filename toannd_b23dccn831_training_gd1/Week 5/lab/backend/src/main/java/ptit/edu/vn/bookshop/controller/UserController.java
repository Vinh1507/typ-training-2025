package ptit.edu.vn.bookshop.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.edu.vn.bookshop.domain.dto.request.UserCreateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.request.UserUpdateRequestDTO;
import ptit.edu.vn.bookshop.domain.dto.response.UserResponseDTO;
import ptit.edu.vn.bookshop.domain.dto.response.page.UserPageResponseDTO;
import ptit.edu.vn.bookshop.service.UserService;
import ptit.edu.vn.bookshop.util.anotation.ApiMessage;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ApiMessage("User created successfully")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        UserResponseDTO userResponseDTO = this.userService.createUser(userCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PutMapping("/users/{id}")
    @ApiMessage("User updated successfully")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateRequestDTO userRequestDTO, @PathVariable Long id) {
        UserResponseDTO userResponseDTO = this.userService.updateUser(userRequestDTO, id);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("User deleted successfully")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    @ApiMessage("User retrieved successfully")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = this.userService.fetchUser(id);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @GetMapping("/users")
    @ApiMessage("Users retrieved successfully")
    public ResponseEntity<UserPageResponseDTO> getAllUsers(Pageable pageable,
                                                           @RequestParam(required = false) String[] user) {
        UserPageResponseDTO userPageResponseDTO = this.userService.fetchAllUsers(pageable, user);
        return ResponseEntity.ok().body(userPageResponseDTO);
    }
}