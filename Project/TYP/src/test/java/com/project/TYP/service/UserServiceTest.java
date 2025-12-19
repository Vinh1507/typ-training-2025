package com.project.TYP.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.TYP.entity.User;
import com.project.TYP.repository.UserRepository;
import com.project.TYP.service.impl.UserServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    // fake
    @Mock
    private UserRepository userRepository;

    // userService(real) + userRepo(fake)
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void createUser_shouldReturnUser_WhenEmailValid() {

        User inputUser = new User(null, "test@gmail.com", null, null);
        User outputUser = new User(1L, "test@gmail.com", null, null);


        when (this.userRepository.existsByEmail(inputUser.getEmail())).thenReturn(false);

        when (this.userRepository.save(any())).thenReturn(outputUser);

        User result = this.userService.createUser(inputUser);

        assertEquals(1L, result.getId());
    }

    @Test
    public void createUser_shouldThrowException_WhenEmailInValid() {

        User inputUser = new User(null, "test@gmail.com", null, null);

        when (this.userRepository.existsByEmail(inputUser.getEmail())).thenReturn(true);

        Exception exc = assertThrows(IllegalArgumentException.class, ()->{
            this.userService.createUser(inputUser);
        });

        assertEquals("Email này đã tồn tại", exc.getMessage());
    }

    @Test
    public void getUserById_shouldReturnOptionalUser() {
        Long inputId = 1L;
        User inputUser = new User(1L, "test@gmail.com", null, null);
        Optional<User> optionalUserTest = Optional.of(inputUser);

        when (this.userRepository.findById(inputId)).thenReturn(optionalUserTest);

        User rs = this.userService.getUserById(inputId);

        assertEquals(inputUser, rs);
    }

    @Test
    public void deleteUserById_shouldReturnException_WhenUserNotExist() {
        Long id = 1L;
        when(this.userRepository.existsById(id)).thenReturn(false);

        Exception exc = assertThrows(EntityNotFoundException.class, ()->{
            this.userService.deleteUserById(id);
        });

        assertEquals("Người dùng không tồn tại", exc.getMessage());
    }

    @Test
    public void deleteUserById_shouldReturnVoid_WhenUserExist() {
        Long id = 1L;
        when(this.userRepository.existsById(id)).thenReturn(true);
        
        this.userService.deleteUserById(id);
        
        verify(this.userRepository).deleteById(1L);
    }

    @Test
    public void updateUser_shouldReturnUser_whenValid() {
        Long id = 1L;
        User inputUser = new User(1L, "oldEmail@gmail.com", null, null);
        User outputUser = new User(1L, "newEmail@gmail.com", null, null);
        when (this.userRepository.findById(id)).thenReturn(Optional.of(inputUser));
        when (this.userRepository.save(any())).thenReturn(outputUser);

        User rs = this.userService.updateUser(id, inputUser);

        assertEquals(outputUser, rs);
    }

}
